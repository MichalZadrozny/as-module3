package pl.michalzadrozny.asmodule3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.michalzadrozny.asmodule3.entity.AppUser;
import pl.michalzadrozny.asmodule3.repository.UserRepo;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final RegistrationService registrationService;

    @Autowired
    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder, RegistrationService registrationService) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.registrationService = registrationService;
    }

    public void addNewUser(AppUser user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        registrationService.sendActivationLink(user.getEmail(), "Link");

        userRepo.save(user);
    }
}
