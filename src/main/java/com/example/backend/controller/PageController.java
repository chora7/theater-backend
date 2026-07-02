/* java.com.example.backend.controller.PageController.java */
package com.example.backend.controller;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.backend.entity.User;
import com.example.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PageController {

    @Autowired
    private UserService userService;
    
    @GetMapping("/login")
    public String loginPage () {
        return "auth/login";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model, Authentication auth) {
        model.addAttribute("username", auth.getName());
        return "admin/dashboard";
    }

    @GetMapping("/user/dashboard")
    public String userDashboard(Model model, Authentication auth) {
        model.addAttribute("username", auth.getName());
        return "user/dashboard";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    @PostMapping("/register")
    public String processRegistration(
            @ModelAttribute User user,
            @RequestParam(defaultValue = "false") boolean isAdmin,
            Model model) {

        // Server-side password validation (never trust client-side only)
        String password = user.getPassword();
        if (password == null || password.length() < 8
                || password.chars().allMatch(Character::isDigit)) {
            model.addAttribute("error",
                "Password must be at least 8 characters and not be entirely numeric.");
            return "auth/register";
        }

        try {
            userService.register(user, isAdmin);
            return "redirect:/login?registered";
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "auth/register";
        }
    }
}
