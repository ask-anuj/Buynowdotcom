package com.dailycodework.buynowdotcom.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment strategy
    private Long id;
    private String username;
    private String firstName;
    private String lastName;

    @NaturalId // Ensures email is unique and can be used for lookups
    private String email;
    private String password;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)// One-to-one relationship with Cart
    // cascade = CascadeType.ALL to propagate all operations (persist, merge, remove, etc.) to the Cart entity
    // orphanRemoval = true to automatically delete the Cart when the User is deleted
    // mappedBy = "user" indicates that the Cart entity owns the relationship
    private Cart cart;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)// One-to-many relationship with Order
    private List<Order> orders;

    //private List<Role> roles;
    @ManyToMany(fetch = FetchType.EAGER, cascade =
            {CascadeType.DETACH, // Ensures that the Role entity is not removed when a User is deleted
                    CascadeType.MERGE, // allows merging of detached Role entities
                    CascadeType.PERSIST, // allows persisting of new Role entities
                    CascadeType.REFRESH} // ensures that the Role entity is refreshed when the User entity is refreshed
    ) // Many-to-many relationship with Role

    @JoinTable(
            name = "user_roles", // Join table name
            joinColumns = @JoinColumn(name = "user_id"), // Foreign key column for User
            inverseJoinColumns = @JoinColumn(name = "role_id", // Foreign key column for Role
                    referencedColumnName = "id") // References the primary key column of the Role entity
    )// Foreign key column for Role

    private Collection<Role> roles = new HashSet<>();
}
