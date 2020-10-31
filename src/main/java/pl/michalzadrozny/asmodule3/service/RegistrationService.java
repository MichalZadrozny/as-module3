package pl.michalzadrozny.asmodule3.service;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.michalzadrozny.asmodule3.entity.AppUser;
import pl.michalzadrozny.asmodule3.entity.Role;
import pl.michalzadrozny.asmodule3.entity.VerificationToken;
import pl.michalzadrozny.asmodule3.repository.UserRepo;
import pl.michalzadrozny.asmodule3.repository.VerificationTokenRepo;

import java.util.Optional;
import java.util.UUID;

@Service
public class RegistrationService {

    @Value("${admin.mail}")
    private String adminEmail;
    private final UserRepo userRepo;
    private final EmailService emailService;
    private final VerificationTokenRepo verificationTokenRepo;

    @Autowired
    public RegistrationService(UserRepo userRepo, EmailService emailService, VerificationTokenRepo verificationTokenRepo) {
        this.userRepo = userRepo;
        this.emailService = emailService;
        this.verificationTokenRepo = verificationTokenRepo;
    }

    public String getContentWithLink(String token, String content) {
        String uri = ServletUriComponentsBuilder.fromCurrentContextPath().pathSegment("verify-token").queryParam("token", token).toUriString();

        return content + uri;
    }

    public String getContentWithAdminLink(String token, String content) {
        String uri = ServletUriComponentsBuilder.fromCurrentContextPath().pathSegment("verify-token").pathSegment("admin").queryParam("token", token).toUriString();

        return content + uri;
    }

    public void verifyToken(String token, boolean verifiedByAdmin) throws NotFoundException {
        Optional<VerificationToken> verificationToken = verificationTokenRepo.findByValue(token);

        if (!verificationToken.isPresent()) {
            throw new NotFoundException("Couldn't found verification token with value: " + token);
        }

        AppUser user = verificationToken.get().getAppUser();

        if (verifiedByAdmin) {
            user.setRole(Role.ROLE_ADMIN);
        } else {
            user.setAccountEnabled(true);
        }

        userRepo.save(user);
        verificationTokenRepo.delete(verificationToken.get());
    }

    public void sendActivationLink(AppUser user, boolean adminRequest) {
        String token = saveVerificationToken(user);

        if (adminRequest) {
            emailService.sendEmail(
                    adminEmail,
                    "Nowy użytkownik poprosił o prawa administratora",
                    getContentWithAdminLink(token, "Klliknij w link poniżej aby aktywować konto użytkownika " + user.getUsername() + ": \n"));
        } else {
            emailService.sendEmail(
                    user.getEmail(),
                    "Twój link aktywacyjny",
                    getContentWithLink(token, "Klliknij w link poniżej aby aktywować konto: \n"));
        }
    }

    private String saveVerificationToken(AppUser user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(user, token);
        verificationTokenRepo.save(verificationToken);
        return token;
    }
}
