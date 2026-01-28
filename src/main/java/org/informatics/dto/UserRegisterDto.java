package org.informatics.dto;

import lombok.*;

/**
 * Data Transfer Object (DTO) used for user registration.
 * Carries registration data from the client to the service layer.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDto {

    // User's first name
    private String firstName;

    // User's last name
    private String lastName;

    // User's email address (used as login identifier)
    private String email;

    // Plain-text password provided during registration
    private String password;

    // Repeated password for confirmation and validation
    private String confirmPassword;

    // Role assigned to the user (e.g. CLIENT, EMPLOYEE, ADMIN)
    private String role;
}

