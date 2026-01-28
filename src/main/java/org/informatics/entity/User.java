package org.informatics.entity;

import jakarta.persistence.*;
import lombok.*;
import org.informatics.entity.enums.Role;

import java.time.LocalDateTime;

/**
 * Entity representing a system User.
 * Users are the base authentication and authorization unit in the system.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "users",
        uniqueConstraints = @UniqueConstraint(columnNames = "email")
)
public class User extends BaseEntity {

    /**
     * User's first name.
     * Cannot be null.
     */
    @Column(nullable = false)
    private String firstName;

    /**
     * User's last name.
     * Cannot be null.
     */
    @Column(nullable = false)
    private String lastName;

    /**
     * User's email address.
     * Must be unique and is used as a login identifier.
     */
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * Hashed user password.
     * Length 60 is typical for BCrypt hashes.
     */
    @Column(name = "password_hash", nullable = false, length = 60)
    private String passwordHash;

    /**
     * Role of the user (e.g. CLIENT, EMPLOYEE, ADMIN).
     * Stored as STRING for readability and stability.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    /**
     * Indicates whether the user account is active.
     * Defaults to true.
     */
    @Column(nullable = false)
    private boolean active = true;

    /**
     * Timestamp of user creation.
     * Set once and never updated.
     */
    @Column(columnDefinition = "DATETIME", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    /**
     * Ensures default values are set before persisting the entity.
     */
    void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (!active) {
            active = true;
        }
    }
}

