package pl.michalzadrozny.asmodule3.service;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.michalzadrozny.asmodule3.entity.AppUser;
import pl.michalzadrozny.asmodule3.entity.VerificationToken;
import pl.michalzadrozny.asmodule3.repository.UserRepo;
import pl.michalzadrozny.asmodule3.repository.VerificationTokenRepo;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final RegistrationService registrationService;
    private final VerificationTokenRepo verificationTokenRepo;

    @Autowired
    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder, RegistrationService registrationService, VerificationTokenRepo verificationTokenRepo) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.registrationService = registrationService;
        this.verificationTokenRepo = verificationTokenRepo;
    }

    public void addNewUser(AppUser user) {

        if (userRepo.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);

        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(user, token);
        verificationTokenRepo.save(verificationToken);

        registrationService.sendActivationLink(user.getEmail(), verificationToken);
    }

    public void verifyToken(String token) throws NotFoundException {
        Optional<VerificationToken> verificationToken = verificationTokenRepo.findByValue(token);

        if (!verificationToken.isPresent()) {
            throw new NotFoundException("Couldn't found verification token with value: " + token);
        }

        AppUser user = verificationToken.get().getAppUser();
        user.setAccountEnabled(true);
        userRepo.save(user);
    }
}
