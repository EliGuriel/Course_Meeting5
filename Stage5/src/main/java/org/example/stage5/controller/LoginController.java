package org.example.stage5.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.stage5.entity.User;
import org.example.stage5.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


/**
 * TODO Stage5 add LoginController handles the login and home page requests.
 * It uses Thymeleaf templates to render the views.
 * so, we use @Controller instead of @RestController
 */

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

    /**
     * TODO Stage5 add login method handles the login page request.
     * @return the name of the login view, it is an HTML page
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * TODO Stage5 add home method handles the home page request.
     * @return the name of the home view, it is an HTML page.
     */
    @GetMapping("/home")
    public String home() {
        return "home";
    }

    /**
     * TODO Stage5 add showAdminHome method handles the admin home page request.
     * @param model the model object to pass data to the view - HTML page
     * @return the name of the admin home view, it is an HTML page.
     */
    @GetMapping("/admin_home")
    public String showAdminHome(@Valid Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin-home";
    }


}
