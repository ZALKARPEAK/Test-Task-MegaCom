package com.career.testtaskmegacom.service.impl;

import com.career.testtaskmegacom.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Override
    public void sendTaskCreatedEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            logger.info("Письмо отправлено на {}", to);
        } catch (Exception e) {
            logger.error("Ошибка при отправке письма: {}", e.getMessage(), e);
        }
    }
}