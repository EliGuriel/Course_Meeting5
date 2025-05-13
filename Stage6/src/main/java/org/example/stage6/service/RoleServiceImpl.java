package org.example.stage6.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.stage6.dto.RoleDto;
import org.example.stage6.entity.Role;
import org.example.stage6.exception.InvalidRequestException;
import org.example.stage6.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

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

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteRole(String roleName) {
        Role role = roleRepository.findByName(roleName);
        if (role == null) {
            throw new InvalidRequestException("Role not found: " + roleName);
        }

        if (!role.getUsers().isEmpty()) {
            throw new InvalidRequestException("Cannot delete role that is assigned to users");
        }

        roleRepository.delete(role);
    }

    @Override
    public Role getRoleByName(String roleName) {
        Role role = roleRepository.findByName(roleName);
        if (role == null) {
            throw new InvalidRequestException("Role not found: " + roleName);
        }
        return role;
    }
}