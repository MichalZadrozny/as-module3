package pl.michalzadrozny.asmodule3.controller;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import pl.michalzadrozny.asmodule3.entity.AppUser;
import pl.michalzadrozny.asmodule3.service.RegistrationService;
import pl.michalzadrozny.asmodule3.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    public ModelAndView register() {
        return new ModelAndView("register", "user", new AppUser());
    }

    @PostMapping("/register")
    public String handleRegister(AppUser user, Model model) {

        try {
            userService.addNewUser(user);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Username is already taken");
            return "/register";
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
