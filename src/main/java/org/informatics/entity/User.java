package org.informatics.entity;

import jakarta.persistence.*;
import lombok.*;
import org.informatics.entity.enums.Role;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User extends BaseEntity{
    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 60)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private boolean active = true;

    @Column(columnDefinition = "DATETIME", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (!active) active = true;
    }
}
