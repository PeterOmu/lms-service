//package com.interswitch.lms.service.auth;
//
//import com.interswitch.lms.dto.request.AuthRequestDTO;
//import com.interswitch.lms.dto.request.RegisterRequestDTO;
//import com.interswitch.lms.dto.response.AuthResponseDTO;
//import com.interswitch.lms.entity.Auth;
//import com.interswitch.lms.entity.Auth.Role;
//import com.interswitch.lms.exception.user.InvalidCredentialsException;
//import com.interswitch.lms.exception.user.UserAlreadyExistsException;
//import com.interswitch.lms.exception.user.UserNotFoundException;
//import com.interswitch.lms.repository.AuthRepository;
//import com.interswitch.lms.security.jwt.JwtService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.util.Set;
//
//@Service
//@RequiredArgsConstructor
//public class AuthServiceImpl implements AuthService {
//
//    private final AuthRepository authRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final JwtService jwtService;
//
//    @Override
//    public AuthResponseDTO register(RegisterRequestDTO request) {
//        if (authRepository.findByEmail(request.getEmail()).isPresent() ||
//                authRepository.findByUsername(request.getUsername()).isPresent()) {
//            throw new UserAlreadyExistsException("User with this email or username already exists.");
//        }
//
//        Auth user = new Auth();
//        user.setEmail(request.getEmail());
//        user.setUsername(request.getUsername());
//        user.setPassword(passwordEncoder.encode(request.getPassword()));
//
//        Role defaultRole = request.getRole() != null ? request.getRole() : Role.STUDENT;
//        user.setRoles(Set.of(defaultRole));
//
//        authRepository.save(user);
//
//        // Use the new method → fixes expiresAt = null
//        JwtService.TokenWithExpiry tokenData = jwtService.generateTokenWithExpiry(
//                user.getEmail(), defaultRole.name());
//
//        return AuthResponseDTO.builder()
//                .token(tokenData.token())
//                .username(user.getUsername())
//                .role(defaultRole.name())
//                .userId(user.getId())
//                .message("Registration successful")
//                .expiresAt(tokenData.expiresAt().toInstant())
//                .build();
//    }
//
//    @Override
//    public AuthResponseDTO authenticate(AuthRequestDTO request) {
//        // Load user first (we'll validate password in controller)
//        Auth user = authRepository.findByEmail(request.getEmail())
//                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + request.getEmail()));
//
//        // Use new method
//        JwtService.TokenWithExpiry tokenData = jwtService.generateTokenWithExpiry(
//                user.getEmail(), user.getRoles().iterator().next().name());
//
//        return AuthResponseDTO.builder()
//                .token(tokenData.token())
//                .username(user.getUsername())
//                .role(user.getRoles().iterator().next().name())
//                .userId(user.getId())
//                .message("Authentication successful")
//                .expiresAt(tokenData.expiresAt().toInstant())   // ← Fixed
//                .build();
//    }
//}

package com.interswitch.lms.service.auth;

import com.interswitch.lms.dto.request.AuthRequestDTO;
import com.interswitch.lms.dto.request.RegisterRequestDTO;
import com.interswitch.lms.dto.response.AuthResponseDTO;
import com.interswitch.lms.entity.Auth;
import com.interswitch.lms.entity.Auth.Role;
import com.interswitch.lms.exception.user.InvalidCredentialsException;
import com.interswitch.lms.exception.user.UserAlreadyExistsException;
import com.interswitch.lms.exception.user.UserNotFoundException;
import com.interswitch.lms.repository.AuthRepository;
import com.interswitch.lms.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public AuthResponseDTO register(RegisterRequestDTO request) {
        if (authRepository.findByEmail(request.getEmail()).isPresent() ||
                authRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("User with this email or username already exists.");
        }

        Auth user = new Auth();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Role defaultRole = request.getRole() != null ? request.getRole() : Role.STUDENT;
        user.setRoles(Set.of(defaultRole));

        authRepository.save(user);

        JwtService.TokenWithExpiry tokenData = jwtService.generateTokenWithExpiry(
                user.getEmail(), defaultRole.name());

        return AuthResponseDTO.builder()
                .token(tokenData.token())
                .username(user.getUsername())
                .role(defaultRole.name())
                .userId(user.getId())
                .message("Registration successful")
                .expiresAt(tokenData.expiresAt().toInstant())
                .build();
    }

    @Override
    public AuthResponseDTO authenticate(AuthRequestDTO request) {
        // Only load user and generate token (no authentication here)
        Auth user = authRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + request.getEmail()));

        JwtService.TokenWithExpiry tokenData = jwtService.generateTokenWithExpiry(
                user.getEmail(), user.getRoles().iterator().next().name());

        return AuthResponseDTO.builder()
                .token(tokenData.token())
                .username(user.getUsername())
                .role(user.getRoles().iterator().next().name())
                .userId(user.getId())
                .message("Authentication successful")
                .expiresAt(tokenData.expiresAt().toInstant())
                .build();
    }
}