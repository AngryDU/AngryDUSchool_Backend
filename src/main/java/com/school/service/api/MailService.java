package com.school.service.api;

public interface MailService {
    void sendEmail(String to, String subject, String text);
}
