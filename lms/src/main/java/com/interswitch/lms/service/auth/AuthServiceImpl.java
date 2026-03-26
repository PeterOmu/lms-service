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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponseDTO register(RegisterRequestDTO request) {
        // Check if email or username exists
        if (authRepository.findByEmail(request.getEmail()).isPresent() ||
                authRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("User with this email or username already exists.");
        }

        // Create Auth entity
        Auth user = new Auth();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Assign default role (wrap in Set)
        Role defaultRole = request.getRole() != null ? request.getRole() : Role.STUDENT;
        user.setRoles(Set.of(defaultRole));

        authRepository.save(user);

        // Generate JWT token
        String token = jwtService.generateToken(user.getEmail(), defaultRole.name());

        // Return structured response
        return AuthResponseDTO.builder()
                .token(token)
                .username(user.getUsername())
                .role(defaultRole.name())
                .userId(user.getId())
                .message("Registration successful")
                .build();
    }

    @Override
    public AuthResponseDTO authenticate(AuthRequestDTO request) {
        // Authenticate credentials
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (Exception e) {
            throw new InvalidCredentialsException("Invalid email or password.");
        }

        // Load user
        Auth user = authRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + request.getEmail()));

        // Generate JWT token
        String token = jwtService.generateToken(user.getUsername(), user.getRoles().iterator().next().name());

        // Return structured response
        return AuthResponseDTO.builder()
                .token(token)
                .username(user.getUsername())
                .role(user.getRoles().iterator().next().name())
                .userId(user.getId())
                .message("Authentication successful")
                .build();
    }
}