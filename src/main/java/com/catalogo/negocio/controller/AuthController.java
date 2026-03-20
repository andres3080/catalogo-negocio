package com.catalogo.negocio.controller;

import com.catalogo.negocio.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final UserService userService;
    private final boolean adminRegistrationEnabled;

    public AuthController(UserService userService,
                          @Value("${app.admin-registration-enabled:false}") boolean adminRegistrationEnabled) {
        this.userService = userService;
        this.adminRegistrationEnabled = adminRegistrationEnabled;
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("registrationVisible", isRegistrationAvailable());
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        if (!isRegistrationAvailable()) {
            return "redirect:/login?registrationDisabled";
        }
        model.addAttribute("registerForm", new RegisterForm());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registerForm") RegisterForm form, BindingResult bindingResult, Model model) {
        if (!isRegistrationAvailable()) {
            return "redirect:/login?registrationDisabled";
        }

        if (!form.getPassword().equals(form.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "password.mismatch", "Las contrasenas no coinciden");
        }

        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            userService.registerAdmin(form.getUsername(), form.getPassword());
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            return "register";
        }
        return "redirect:/login?registered";
    }

    private boolean isRegistrationAvailable() {
        return adminRegistrationEnabled && !userService.hasAdminUsers();
    }

    public static class RegisterForm {
        @NotBlank
        @Size(min = 4, max = 60)
        private String username = "";

        @NotBlank
        @Size(min = 6, max = 80)
        private String password = "";

        @NotBlank
        @Size(min = 6, max = 80)
        private String confirmPassword = "";

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getConfirmPassword() {
            return confirmPassword;
        }

        public void setConfirmPassword(String confirmPassword) {
            this.confirmPassword = confirmPassword;
        }
    }
}
