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
    private final JavaMailSender emailSender;

    public void sendEmail(String toEmail, String title, String content) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(toEmail);
        helper.setSubject(title);
        // true를 설정해서 HTML을 사용 가능하게 함
        helper.setText(content, true);
        helper.setFrom("inforestall@gmail.com");

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
