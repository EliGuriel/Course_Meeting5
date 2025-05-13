package org.example.stage5.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.stage5.dto.UserDto;
import org.example.stage5.dto.UserResponseDto;
import org.example.stage5.response.StandardResponse;
import org.example.stage5.service.UserServiceImpl;
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

    private final UserServiceImpl userServiceImpl;

    @GetMapping("/hello")
    public ResponseEntity<StandardResponse> hello() {
        StandardResponse response = new StandardResponse("success", "Hello, World!", null);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status")
    public ResponseEntity<StandardResponse> status() {
        String username = userServiceImpl.getCurrentUsername();
        StandardResponse response = new StandardResponse("success",
                "You are logged in as User name: " + username + "!",
                null);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<StandardResponse> register(@Valid @RequestBody UserDto userDto) {
        UserResponseDto registeredUser = userServiceImpl.registerUser(userDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .buildAndExpand(registeredUser)
                .toUri();

        StandardResponse response = new StandardResponse("success", registeredUser, null);
        return ResponseEntity.created(location)
                .body(response);
    }

}