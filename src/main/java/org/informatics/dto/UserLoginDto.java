package org.informatics.dto;

import lombok.*;

/**
 * Data Transfer Object (DTO) used for user authentication.
 * Carries login credentials from the client to the service layer.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDto {

    // User email used for authentication
    private String email;

    // Plain-text password submitted during login
    // (will be validated and compared after hashing)
    private String password;
}

