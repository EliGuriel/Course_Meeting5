package org.example.stage4.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.stage4.dto.RoleDto;
import org.example.stage4.entity.Role;
import org.example.stage4.exception.InvalidRequestException;
import org.example.stage4.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService{

    private final RoleRepository roleRepository;

    @Transactional
    public RoleDto addRole(String role) {
        // check if a role already exists
        if (roleRepository.findByName(role) != null) {
            throw new InvalidRequestException("Role " + role + " already exists");
        }

        role = role.toUpperCase();
        roleRepository.save(new Role(null, role, null));

        // create a RoleDto object to return

        RoleDto roleDto = new RoleDto();
        roleDto.setRoleName(role);
        return roleDto;
    }

}
