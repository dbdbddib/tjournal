package com.example.tjournal.emailapi;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class EmailService {
    // EmailConfig에서 설정한 JavaMailSender 빈이 EmailService의 생성자(또는 @RequiredArgsConstructor로 주입받은)에서 주입되어 사용

    private final JavaMailSender emailSender;

    public void sendEmail(String toEmail, String title, String content) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();

        // MIME(Multipurpose Internet Mail Extensions)
        // 텍스트뿐만 아니라 HTML, 이미지, 첨부파일 등 다양한 콘텐츠 형식을 지원하는 이메일 포맷
        // JavaMailSender 인터페이스에서 제공
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(toEmail);
        helper.setSubject(title);
        // true를 설정해서 HTML을 사용 가능하게 함
        helper.setText(content, true);
        helper.setFrom("qudgns4475@gmail.com");

        try {
            emailSender.send(message);
        } catch (RuntimeException e) {
            throw new RuntimeException("Unable to send email in sendEmail", e);
        }
    }

    public SimpleMailMessage createEmailFrom(String toEmail, String title, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(title);
        message.setText(text);
        return message;
    }

}
