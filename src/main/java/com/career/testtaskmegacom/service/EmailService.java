package com.career.testtaskmegacom.service;

public interface EmailService {
    void sendTaskCreatedEmail(String to, String subject, String body);
}
