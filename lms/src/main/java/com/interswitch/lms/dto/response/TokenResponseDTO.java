package com.interswitch.lms.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponseDTO {

    private Long id;

    private String token;

    private boolean revoked;

    private boolean expired;

    private LocalDateTime createdAt;
}