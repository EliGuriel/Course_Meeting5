package org.example.stage6.service;

import org.example.stage6.dto.RoleDto;
import org.example.stage6.entity.Role;
import org.example.stage6.exception.InvalidRequestException;

import java.util.List;

public interface RoleService {

    /**
     * Add a new role to the system
     * @param roleName the name of the role to add
     * @return the created RoleDto
     * @throws InvalidRequestException if the role already exists
     */
    RoleDto addRole(String roleName);

    /**
     * Get all roles in the system
     * @return a list of all roles
     */
    List<Role> getAllRoles();

    /**
     * Delete a role from the system
     * @param roleName the name of the role to delete
     * @throws InvalidRequestException if the role doesn't exist or has users
     */
    void deleteRole(String roleName);

    /**
     * Get a role by its name
     * @param roleName the name of the role to find
     * @return the Role entity
     * @throws InvalidRequestException if the role doesn't exist
     */
    Role getRoleByName(String roleName);
}