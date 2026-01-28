package org.informatics.dto;

import lombok.*;

/**
 * Data Transfer Object (DTO) used to store authenticated user data
 * in the session after successful login.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSessionDto {

    // Unique identifier of the authenticated user
    private long userId;

    // Email of the logged-in user
    private String email;

    // Role of the user (e.g. CLIENT, EMPLOYEE, ADMIN)
    private String role;
}

