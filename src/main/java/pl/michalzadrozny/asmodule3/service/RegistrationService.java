package pl.michalzadrozny.asmodule3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    private final EmailService emailService;

    @Autowired
    public RegistrationService(EmailService emailService) {
        this.emailService = emailService;
    }

    public void sendActivationLink(String email, String link) {
        emailService.sendEmail(email, "Twój link aktywacyjny", "Klliknij w link poniżej aby aktywować konto: \n" + link);
    }
}
