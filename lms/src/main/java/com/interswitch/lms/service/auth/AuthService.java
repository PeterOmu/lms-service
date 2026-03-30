package com.interswitch.lms.service.auth;


import com.interswitch.lms.dto.request.AuthRequestDTO;
import com.interswitch.lms.dto.request.RegisterRequestDTO;
import com.interswitch.lms.dto.response.AuthResponseDTO;

public interface AuthService {

    AuthResponseDTO register(RegisterRequestDTO request);
    AuthResponseDTO loginUser(AuthRequestDTO request);
}
