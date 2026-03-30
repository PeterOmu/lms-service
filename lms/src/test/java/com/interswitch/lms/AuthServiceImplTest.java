package com.interswitch.lms;

import com.interswitch.lms.dto.request.RegisterRequestDTO;
import com.interswitch.lms.dto.request.AuthRequestDTO;
import com.interswitch.lms.dto.response.AuthResponseDTO;
import com.interswitch.lms.entity.Auth;
import com.interswitch.lms.entity.Department;
import com.interswitch.lms.exception.user.UserAlreadyExistsException;
import com.interswitch.lms.repository.AuthRepository;
import com.interswitch.lms.repository.StudentRepository;
import com.interswitch.lms.repository.TeacherRepository;
import com.interswitch.lms.repository.DepartmentRepository;
import com.interswitch.lms.security.jwt.JwtService;
import com.interswitch.lms.service.auth.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {

    @Mock
    private AuthRepository authRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_SuccessfulStudentRegistration() {

        RegisterRequestDTO dto = RegisterRequestDTO.builder()
                .username("alice")
                .email("alice@example.com")
                .password("password123")
                .role(Auth.Role.STUDENT)
                .departmentId(1L)
                .build();

        // Mock department
        Department department = new Department();
        department.setId(1L);
        department.setName("Computer Science");

        when(departmentRepository.findById(1L))
                .thenReturn(Optional.of(department));

        when(authRepository.findByEmail(dto.getEmail()))
                .thenReturn(Optional.empty());

        when(authRepository.findByUsername(dto.getUsername()))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode(dto.getPassword()))
                .thenReturn("hashedPassword");

        when(authRepository.save(any(Auth.class)))
                .thenAnswer(i -> i.getArgument(0));

        when(jwtService.generateTokenWithExpiry(anyString(), anyString()))
                .thenReturn(new JwtService.TokenWithExpiry(
                        "token",
                        java.time.Instant.now().plusSeconds(3600)
                ));

        AuthResponseDTO response = authService.register(dto);

        assertNotNull(response);
        assertEquals("alice@example.com", response.getUsername());

        verify(authRepository, times(1)).save(any(Auth.class));
    }

    @Test
    void register_ThrowsUserAlreadyExists() {
        RegisterRequestDTO dto = RegisterRequestDTO.builder()
                .username("alice")
                .email("alice@example.com")
                .build();

        when(authRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(new Auth()));

        assertThrows(UserAlreadyExistsException.class, () -> authService.register(dto));
    }

    @Test
    void loginUser_SuccessfulLogin() {
        AuthRequestDTO dto = new AuthRequestDTO();
        dto.setEmail("bob@example.com");
        dto.setPassword("password123");

        Auth user = new Auth();
        user.setEmail(dto.getEmail());
        user.setPassword("hashedPassword");
        user.setUsername("bob");
        user.setRoles(Set.of(Auth.Role.STUDENT));

        when(authRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(dto.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtService.generateTokenWithExpiry(anyString(), anyString()))
                .thenReturn(new JwtService.TokenWithExpiry("token", java.time.Instant.now().plusSeconds(3600)));

        AuthResponseDTO response = authService.loginUser(dto);

        assertNotNull(response);
        assertEquals("bob@example.com", response.getUsername());
        assertEquals("STUDENT", response.getRole());
    }
}