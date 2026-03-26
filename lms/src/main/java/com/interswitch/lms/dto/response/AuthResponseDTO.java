package com.interswitch.lms.dto.response;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDTO {

    private String token;
    private String username;
    private Long userId;
    private String role;
    private String message;
    private Instant expiresAt;
}