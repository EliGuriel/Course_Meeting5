package org.example.stage6.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private String username;
    @Column(nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    //@ManyToMany
    @JsonIgnore // to prevent infinite recursion, in case of bidirectional relationship
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "username"),
            inverseJoinColumns = @JoinColumn(name = "role_id"),
            // Ensure that a user can have a role only once, the user_id and role_id combination must be unique
            uniqueConstraints = @UniqueConstraint(columnNames = {"username", "role_id"}))

    @ToString.Exclude
    private List<Role> roles = new ArrayList<>();

}
