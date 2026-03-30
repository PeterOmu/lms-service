package com.interswitch.lms.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * DTO for user login/authentication requests
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequestDTO {

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;


}