package pl.michalzadrozny.asmodule3.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class AppController {

    @GetMapping("/forAll")
    public String forAll() {
        return "Witamy na naszej stronie!";
    }

    @GetMapping("/forAdmin")
    public String forAdmin(Principal principal) {
        return "Admin: " + principal.getName();
    }

    @GetMapping("/forUser")
    public String forUser(Principal principal) {
        return "Użytkownik: " + principal.getName();
    }

    @GetMapping("/logged-out")
    public String loggedOut() {
        return "Papa ";
    }

}
