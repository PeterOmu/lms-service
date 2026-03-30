////package com.interswitch.lms.controller;
////
////import com.interswitch.lms.dto.request.AuthRequestDTO;
////import com.interswitch.lms.dto.request.RegisterRequestDTO;
////import com.interswitch.lms.dto.response.AuthResponseDTO;
////import com.interswitch.lms.dto.response.HealthResponseDTO;
////import com.interswitch.lms.service.auth.AuthService;
////import jakarta.validation.Valid;
////import lombok.RequiredArgsConstructor;
////import org.springframework.http.ResponseEntity;
////import org.springframework.security.authentication.AuthenticationManager;
////import org.springframework.web.bind.annotation.*;
////
////import java.util.Date;
////
////@RestController
////@RequestMapping("/api/auth")
////@RequiredArgsConstructor
////public class AuthController {
////    private final AuthenticationManager authenticationManager;
////    private final AuthService authService;
////
////    /**
////     * Register a new user
////     */
////    @PostMapping("/register")
////    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
////        AuthResponseDTO response = authService.register(request);
////        return ResponseEntity.ok(response);
////    }
////
////    /**
////     * Authenticate a user and return JWT token
////     */
////    @PostMapping("/login")
////    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO request) {
////        AuthResponseDTO response = authService.authenticate(request);
////        return ResponseEntity.ok(response);
////    }
////
////    /**
////     * Health check endpoint for auth service
////     */
////    @GetMapping("/health")
////    public HealthResponseDTO healthCheck(){
////
////        return HealthResponseDTO.builder()
////                .status("UP")
////                .service("Auth Service running")
////                .timestamp(new Date())
////                .build();
////    }
////}
//
//package com.interswitch.lms.controller;
//
//import com.interswitch.lms.dto.request.AuthRequestDTO;
//import com.interswitch.lms.dto.request.RegisterRequestDTO;
//import com.interswitch.lms.dto.response.AuthResponseDTO;
//import com.interswitch.lms.dto.response.HealthResponseDTO;
//import com.interswitch.lms.service.auth.AuthService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Date;
//
//@RestController
//@RequestMapping("/api/auth")
//@RequiredArgsConstructor
//public class AuthController {
//
//    private final AuthenticationManager authenticationManager;
//    private final AuthService authService;
//
//    @PostMapping("/register")
//    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
//        AuthResponseDTO response = authService.register(request);
//        return ResponseEntity.ok(response);
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO request) {
//        try {
//            // Perform authentication
//            authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                            request.getEmail(),
//                            request.getPassword()
//                    )
//            );
//
//            // If successful, generate response via service
//            AuthResponseDTO response = authService.authenticate(request);
//            return ResponseEntity.ok(response);
//
//        } catch (BadCredentialsException ex) {
//            return ResponseEntity.badRequest().body(
//                    AuthResponseDTO.builder()
//                            .message("Invalid email or password")
//                            .build()
//            );
//        } catch (Exception ex) {
//            return ResponseEntity.internalServerError().body(
//                    AuthResponseDTO.builder()
//                            .message("Authentication failed: " + ex.getMessage())
//                            .build()
//            );
//        }
//    }
//
//    @GetMapping("/health")
//    public HealthResponseDTO healthCheck() {
//        return HealthResponseDTO.builder()
//                .status("UP")
//                .service("Auth Service running")
//                .timestamp(new Date())
//                .build();
//    }
//}

package com.interswitch.lms.controller;

import com.interswitch.lms.dto.request.AuthRequestDTO;
import com.interswitch.lms.dto.request.LoginRequestDTO;
import com.interswitch.lms.dto.request.RegisterRequestDTO;
import com.interswitch.lms.dto.response.AuthResponseDTO;
import com.interswitch.lms.dto.response.HealthResponseDTO;
import com.interswitch.lms.service.auth.AuthService;
import com.interswitch.lms.exception.user.InvalidCredentialsException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
        AuthResponseDTO response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> loginUser(@RequestBody @Valid AuthRequestDTO loginRequest) {
        return ResponseEntity.ok(authService.loginUser(loginRequest));
    }
    @GetMapping("/health")
    public HealthResponseDTO healthCheck() {
        return HealthResponseDTO.builder()
                .status("UP")
                .service("Auth Service running")
                .timestamp(new Date())
                .build();
    }
}