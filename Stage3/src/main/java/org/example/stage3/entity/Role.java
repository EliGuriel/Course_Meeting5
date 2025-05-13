package org.example.stage3.entity;

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
public class Role {
    @Id
    // make auto increment, GenerationType.IDENTITY is used to make the id auto increment
    // it is different from AUTO, which is used to make the id auto increment in a different way
    // GenerationType.AUTO is not recommended for MySQL
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false, length = 20)
    private String name;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER) // mappedBy is used to prevent the creation of a join table
    @JsonIgnore // to prevent infinite recursion, in case of bidirectional relationship

    @ToString.Exclude
    private List<User> users = new ArrayList<>();



}
