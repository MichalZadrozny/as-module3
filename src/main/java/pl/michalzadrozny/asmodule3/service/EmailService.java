package pl.michalzadrozny.asmodule3.service;

public interface EmailService {
    void sendEmail(String to, String subject, String content);
}