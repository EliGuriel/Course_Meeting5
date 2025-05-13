package org.example.stage4.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.stage4.dto.RoleDto;
import org.example.stage4.response.StandardResponse;
import org.example.stage4.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleServiceIImplImpl;

    @PostMapping("/role")
    public ResponseEntity<StandardResponse> createRole(@Valid @RequestBody RoleDto roleDto) {
        RoleDto roleName = roleServiceIImplImpl.addRole(roleDto.getRoleName());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .buildAndExpand(roleName)
                .toUri();

        StandardResponse response = new StandardResponse("success", roleName, null);

        return ResponseEntity.created(location)
                .body(response);
    }
}