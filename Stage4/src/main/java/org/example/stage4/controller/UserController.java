package org.example.stage4.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.stage4.dto.UserDto;
import org.example.stage4.dto.UserResponseDto;
import org.example.stage4.response.StandardResponse;
import org.example.stage4.service.UserServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

    @GetMapping("/hello")
    public ResponseEntity<StandardResponse> hello() {
        StandardResponse response = new StandardResponse("success", "Hello, World!", null);
        return ResponseEntity.ok(response);
    }

    @GetMapping({"/home", "/"})
    public ResponseEntity<StandardResponse> home() {
        StandardResponse response = new StandardResponse("success", "Welcome to the home page!", null);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status")
    public ResponseEntity<StandardResponse> status() {
        String username = userService.getCurrentUsername();
        StandardResponse response = new StandardResponse("success",
                "You are logged in as User name: " + username + "!",
                null);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<StandardResponse> register(@Valid @RequestBody UserDto userDto) {
        UserResponseDto registeredUser = userService.registerUser(userDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .buildAndExpand(registeredUser)
                .toUri();

        StandardResponse response = new StandardResponse("success", registeredUser, null);
        return ResponseEntity.created(location)
                .body(response);
    }

    @GetMapping("/admin_home")
    public ResponseEntity<StandardResponse> adminHome() {
        StandardResponse response = new StandardResponse("success", "Welcome to the admin home page!", null);
        return ResponseEntity.ok(response);
    }

    /*
        * This is not a real login endpoint. It's just a placeholder to show how to handle login.
        * Real login would be a POST request with credentials.
        * This is GET Mapping to show a message that login is required.
     */
    @GetMapping("/login")
    public ResponseEntity<StandardResponse> showLogin() {
        StandardResponse response = new StandardResponse("success", "Please login", null);
        return ResponseEntity.ok(response);
    }
}