package org.example.stage6.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.stage6.dto.RoleDto;
import org.example.stage6.dto.UserDto;
import org.example.stage6.entity.Role;
import org.example.stage6.entity.User;
import org.example.stage6.exception.InvalidRequestException;
import org.example.stage6.service.RoleService;
import org.example.stage6.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

/**
 * AdminController handles all administrative operations for user and role management.
 * This controller is protected by Spring Security and requires admin privileges to access.
 * It relies on the AdminExceptionHandler for specialized error handling.
 */
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    /**
     * Displays the admin dashboard with lists of all users and roles.
     * 
     * @param model The model to pass data to the view
     * @return The admin home view name
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<User> users = userService.getAllUsers();
        List<Role> roles = roleService.getAllRoles();
        model.addAttribute("users", users);
        model.addAttribute("roles", roles);
        return "admin-home";
    }

    /**
     * Displays the form for adding a new user.
     * 
     * @param model The model to pass data to the view
     * @return They add user form view name
     */
    @GetMapping("/add-user")
    public String showAddUserForm(Model model) {
        model.addAttribute("userDto", new UserDto());
        model.addAttribute("availableRoles", roleService.getAllRoles());
        return "add-user";
    }

    /**
     * Processes the submission of the added user form.
     * Validation errors are handled by Spring's validation mechanism.
     * Business logic errors are thrown as InvalidRequestException and handled by AdminExceptionHandler.
     * 
     * @param userDto The user data transfer object from the form
     * @param bindingResult The binding result for validation errors
     * @param redirectAttributes For success messages on redirect
     * @param model The model to pass data to the view in case of errors
     * @return Redirect to the dashboard on success or back to form on error
     */
    @PostMapping("/add-user")
    public String addUser(@Valid @ModelAttribute UserDto userDto,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes,
                          Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("availableRoles", roleService.getAllRoles());
            return "add-user";
        }

        // We still use try-catch here for explicit error handling in the view
        // In a future refactoring, this could be removed and handled entirely by AdminExceptionHandler
        try {
            userService.registerUser(userDto);
            redirectAttributes.addFlashAttribute("success", "המשתמש נוצר בהצלחה");
            return "redirect:/admin/dashboard";
        } catch (InvalidRequestException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("availableRoles", roleService.getAllRoles());
            return "add-user";
        }
    }

    /**
     * Displays the form for editing an existing user.
     * 
     * @param username The username of the user to edit
     * @param model The model to pass data to the view
     * @return The edit user forms a view name or redirects to dashboard if user not found
     */
    @GetMapping("/edit-user/{username}")
    public String showEditUserForm(@PathVariable String username, Model model) {
        try {
            User user = userService.getUserByUsername(username);
            UserDto userDto = new UserDto();
            userDto.setUsername(user.getUsername());
            userDto.setPassword(""); // We don't display the password

            List<String> userRoles = user.getRoles().stream()
                    .map(Role::getName)
                    .collect(Collectors.toList());

            model.addAttribute("userDto", userDto);
            model.addAttribute("userRoles", userRoles);
            model.addAttribute("availableRoles", roleService.getAllRoles());

            return "edit-user";
        } catch (InvalidRequestException e) {
            // In a future refactoring, we could throw the exception and let AdminExceptionHandler manage it
            return "redirect:/admin/dashboard";
        }
    }

    /**
     * Processes the submission of the edit user form.
     * 
     * @param username The username of the user being edited
     * @param userDto The user data transfer object from the form
     * @param bindingResult The binding result for validation errors
     * @param redirectAttributes For success messages on redirect
     * @param model The model to pass data to the view in case of errors
     * @return Redirect to the dashboard on success or back to form on error
     */
    @PostMapping("/edit-user/{username}")
    public String editUser(@PathVariable String username,
                           @ModelAttribute UserDto userDto,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes,
                           Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("availableRoles", roleService.getAllRoles());
            return "edit-user";
        }

        try {
            userService.updateUser(userDto);
            redirectAttributes.addFlashAttribute("success", "המשתמש עודכן בהצלחה");
            return "redirect:/admin/dashboard";
        } catch (InvalidRequestException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("availableRoles", roleService.getAllRoles());
            return "edit-user";
        }
    }

    /**
     * Displays a confirmation page before deleting a user.
     * 
     * @param username The username of the user to delete
     * @param model The model to pass data to the view
     * @return The delete confirmation view
     */
    @GetMapping("/delete-user/{username}")
    public String showDeleteUserConfirmation(@PathVariable String username, Model model) {
        model.addAttribute("id", username);
        model.addAttribute("type", "user");
        return "delete-confirmation";
    }

    /**
     * Processes the user deletion after confirmation.
     * 
     * @param username The username of the user to delete
     * @param redirectAttributes For success or error messages on redirect
     * @return Redirect to dashboard
     */
    @PostMapping("/delete-user/{username}/confirm")
    public String deleteUser(@PathVariable String username, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(username);
            redirectAttributes.addFlashAttribute("success", "המשתמש נמחק בהצלחה");
        } catch (InvalidRequestException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/dashboard";
    }

    /**
     * Displays the form for adding a new role.
     * 
     * @param model The model to pass data to the view
     * @return The adding role form view
     */
    @GetMapping("/add-role")
    public String showAddRoleForm(Model model) {
        model.addAttribute("roleDto", new RoleDto());
        return "add-role";
    }

    /**
     * Processes the submission of the add role form.
     * 
     * @param roleDto The role data transfer object from the form
     * @param bindingResult The binding result for validation errors
     * @param redirectAttributes For success messages on redirect
     * @param model The model to pass data to the view in case of errors
     * @return Redirect to the dashboard on success or back to form on error
     */
    @PostMapping("/add-role")
    public String addRole(@Valid @ModelAttribute RoleDto roleDto,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes,
                          Model model) {

        if (bindingResult.hasErrors()) {
            return "add-role";
        }

        try {
            roleService.addRole(roleDto.getRoleName());
            redirectAttributes.addFlashAttribute("success", "התפקיד נוצר בהצלחה");
            return "redirect:/admin/dashboard";
        } catch (InvalidRequestException e) {
            model.addAttribute("error", e.getMessage());
            return "add-role";
        }
    }

    /**
     * Displays a confirmation page before deleting a role.
     * 
     * @param roleName The name of the role to delete
     * @param model The model to pass data to the view
     * @return The delete confirmation view
     */
    @GetMapping("/delete-role/{roleName}")
    public String showDeleteRoleConfirmation(@PathVariable String roleName, Model model) {
        model.addAttribute("id", roleName);
        model.addAttribute("type", "role");
        return "delete-confirmation";
    }

    /**
     * Processes the role deletion after confirmation.
     * 
     * @param roleName The name of the role to delete
     * @param redirectAttributes For success or error messages on redirect
     * @return Redirect to dashboard
     */
    @PostMapping("/delete-role/{roleName}/confirm")
    public String deleteRole(@PathVariable String roleName, RedirectAttributes redirectAttributes) {
        try {
            roleService.deleteRole(roleName);
            redirectAttributes.addFlashAttribute("success", "התפקיד נמחק בהצלחה");
        } catch (InvalidRequestException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/dashboard";
    }
}