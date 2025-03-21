package com.example.tjournal.emailapi;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
public class MemberEmailService {
    @Autowired
    private EmailService emailService;
    @Autowired
    private IEmailMybatisMapper emailMapper;

    @Transactional
    public void sendVerifyCode(String email) {
        emailMapper.deleteByEmail(email);
        sendCodeToEmail(email);
    }

    public void sendCodeToEmail(String email) {
        EmailDto createdCode = createVerificationCode(email);
        String content = "<html>"
                + "<body>"
                + "<h1>TJournal 이메일 인증 코드 :</h1>"
                + "<p>아래 코드를 입력해주세요 : " + createdCode.getCode() + "</p>"
                + "</body>"
                + "</html>";

        try {
            emailService.sendEmail(email, "TJournal 이메일 인증 안내", content);
        } catch (RuntimeException | MessagingException e) {
            log.error("Unable to send email in sendCodeToEmail", e);
            throw new RuntimeException("Unable to send email in sendCodeToEmail", e);
        }
    }

    // 인증 코드 생성 및 저장
    private EmailDto createVerificationCode(String email) {
        String randomCode = generateRandomCode(6);
        EmailDto code = EmailDto.builder()
                .email(email)
                .code(randomCode)
                .expireTime(LocalDateTime.now().plusDays(1)) // 1일 후 만료
                .build();
        emailMapper.save(code);
        return code;
    }

    // 랜덤 코드 생성
    public String generateRandomCode(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";
        StringBuilder sb = new StringBuilder();
        ThreadLocalRandom random = ThreadLocalRandom.current();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }

    // 이메일 전송 1분이 지나는지 여부
    // 1분이 지났음 -> true
    public long getRemainingDelaySeconds(String email) {
        EmailDto emailDto = emailMapper.findByEmail(email);
        if (emailDto == null) {
            return 0;
        }
        LocalDateTime creationTime = emailDto.getExpireTime().minusHours(24);
        LocalDateTime allowedTime = creationTime.plusMinutes(1);
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(allowedTime)) {
            // 남은 시간 계산 (초 단위)
            Duration remaining = Duration.between(now, allowedTime);
            return remaining.getSeconds();
        }
        return 0;
    }

    // 인증 코드 검증
    public boolean verifyCode(String email, String code) {
        return emailMapper.findByEmailAndCode(email, code)
                .map(vc -> vc.getExpireTime().isAfter(LocalDateTime.now()))
                .orElse(false);
        // emailMapper의 반환값이 비워 있으면 orElse(false) 반환
        // 비워 있지 않으면 map(vc -> vc.getExpireTime().isAfter(LocalDateTime.now())) 실행
        // vc는 Optional<VerificationCode> 안에 들어 있는 실제 객체, 즉 EmailDto 인스턴스
    }

    @Transactional
    @Scheduled(cron = "0 0 12 * * ?")  // 매일 12시에 만료된 인증 코드 삭제
    public void deleteExpiredVerificationCodes() {
        emailMapper.deleteByExpireTimeBefore(LocalDateTime.now());
    }

}
