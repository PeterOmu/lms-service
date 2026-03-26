package com.interswitch.lms.service.auth;


import com.interswitch.lms.dto.request.AuthRequestDTO;
import com.interswitch.lms.dto.request.RegisterRequestDTO;
import com.interswitch.lms.dto.response.AuthResponseDTO;

public interface AuthService {

    /**
     * Registers a new user (student, teacher, or admin)
     */
    AuthResponseDTO register(RegisterRequestDTO request);

    /**
     * Authenticates a user and returns JWT token
     */
    AuthResponseDTO authenticate(AuthRequestDTO request);
}
