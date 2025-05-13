package org.example.stage6.controller;

import lombok.RequiredArgsConstructor;
import org.example.stage6.dto.RoleDto;
import org.example.stage6.entity.Role;
import org.example.stage6.entity.User;
import org.example.stage6.service.RoleService;
import org.example.stage6.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;
    private final RoleService roleService;

    /**
     * TODO Stage5 add login method handles the login page request.
     *
     * @return the name of the login view, it is an HTML page
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * TODO Stage5 add home method handles the home page request.
     *
     * @return the name of the home view, it is an HTML page.
     */

    @GetMapping("/home")
    public String home() {
        return "home";
    }


    /**
     * TODO Stage5 add showAdminHome method handles the admin home page request.
     *
     * @param model the model object to pass data to the view - HTML page
     * @return the name of the admin home view, it is an HTML page.
     */
    @GetMapping("/admin_home")
    public String adminHome(Model model) {
        // קודם כל טען את הרולים

        List<Role> roles = roleService.getAllRoles();

        List<User> users = userService.getAllUsers();


        // הוסף למודל בסדר הנכון - קודם רולים ואז משתמשים
        model.addAttribute("roles", roles);
        model.addAttribute("users", users);

        // הוסף את roleDto עבור הטופס המוטבע
        model.addAttribute("roleDto", new RoleDto());

        return "admin-home";
    }

}
