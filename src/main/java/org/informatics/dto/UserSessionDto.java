package org.informatics.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSessionDto {
    private long userId;
    private String email;
    private String role;
}
