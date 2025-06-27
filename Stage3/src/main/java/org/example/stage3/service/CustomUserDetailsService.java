package org.example.stage3.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.stage3.entity.Role;
import org.example.stage3.entity.User;
import org.example.stage3.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findById(username);
        User user = userOptional.orElse(null);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username.");
        }

        UserDetails userDetails =  new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(), // Spring Security does the checking of the password, no need to do it manually
                mapRolesToAuthorities(user.getRoles())
        );
        // if the user is locked (disabled), throw an exception
        if (!userDetails.isEnabled()) {
            throw new UsernameNotFoundException("User is locked.");
        }

        // if the user is expired, throw an exception
        if (!userDetails.isAccountNonExpired()) {
            throw new UsernameNotFoundException("User account has expired.");
        }

        return userDetails;
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(List<Role> roles) {
        return roles.stream()
                // add the prefix "ROLE_" to the role name, it is required by Spring Security, ADMIN=> ROLE_ADMIN
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(Collectors.toList());
    }
}
