package pl.michalzadrozny.asmodule3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.michalzadrozny.asmodule3.entity.VerificationToken;

@Service
public class RegistrationService {

    private final EmailService emailService;

    @Autowired
    public RegistrationService(EmailService emailService) {
        this.emailService = emailService;
    }

    public void sendActivationLink(String email, VerificationToken token) {

        String uri = ServletUriComponentsBuilder.fromCurrentRequest().replacePath("verify-token").queryParam("token", token.getValue()).toUriString();

        emailService.sendEmail(email, "Twój link aktywacyjny", "Klliknij w link poniżej aby aktywować konto: \n" + uri);
    }
}
