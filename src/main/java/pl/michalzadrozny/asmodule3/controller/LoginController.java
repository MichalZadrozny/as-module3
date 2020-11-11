package pl.michalzadrozny.asmodule3.controller;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.michalzadrozny.asmodule3.entity.AppUser;
import pl.michalzadrozny.asmodule3.service.RegistrationService;
import pl.michalzadrozny.asmodule3.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Slf4j
@Controller
public class LoginController {

    private final UserService userService;
    private final RegistrationService registrationService;

    @Autowired
    public LoginController(UserService userService, RegistrationService registrationService) {
        this.userService = userService;
        this.registrationService = registrationService;
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) boolean activated, Model model) {

        if (activated) {
            model.addAttribute("activated", "Account has been activated");
        }
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        return "redirect:/login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        AppUser user = new AppUser();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/register")
    public String handleRegister(@Valid @ModelAttribute("user") AppUser user, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "register";
        }

        try {
            userService.addNewUser(user);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Username is already taken");
            return "register";
        }

        return "redirect:/login";
    }

    @GetMapping("/verify-token")
    public String verifyToken(@RequestParam String token) {
        try {
            registrationService.verifyToken(token, false);
        } catch (NotFoundException e) {
            e.printStackTrace();
            return "redirect:/login";
        }

        return "redirect:/login?activated=true";
    }

    @GetMapping("/verify-token/admin")
    public String verifyAdminToken(@RequestParam String token) {
        try {
            registrationService.verifyToken(token, true);
        } catch (NotFoundException e) {
            e.printStackTrace();
            return "redirect:/login";
        }

        return "redirect:/login?activated=true";
    }
}
