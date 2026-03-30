package com.interswitch.lms.service.auth;

import com.interswitch.lms.dto.request.AuthRequestDTO;
import com.interswitch.lms.dto.request.RegisterRequestDTO;
import com.interswitch.lms.dto.response.AuthResponseDTO;
import com.interswitch.lms.entity.Auth;
import com.interswitch.lms.entity.Auth.Role;
import com.interswitch.lms.entity.Student;
import com.interswitch.lms.entity.Teacher;
import com.interswitch.lms.entity.Department;
import com.interswitch.lms.exception.ResourceNotFoundException;
import com.interswitch.lms.exception.user.InvalidCredentialsException;
import com.interswitch.lms.exception.user.UserAlreadyExistsException;
import com.interswitch.lms.exception.user.UserNotFoundException;
import com.interswitch.lms.repository.AuthRepository;
import com.interswitch.lms.repository.StudentRepository;
import com.interswitch.lms.repository.TeacherRepository;
import com.interswitch.lms.repository.DepartmentRepository;
import com.interswitch.lms.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    @Transactional
    public AuthResponseDTO register(RegisterRequestDTO request) {

        // Check if user/email exists
        if (authRepository.findByEmail(request.getEmail()).isPresent() ||
                authRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("User with this email or username already exists.");
        }

        // Set role, default = STUDENT
        Role role = request.getRole() != null ? request.getRole() : Role.STUDENT;

        // Create Auth user
        Auth user = new Auth();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(Set.of(role));

        Auth savedAuth = authRepository.save(user);

        // Automatically create Student or Teacher entity if role is STUDENT or TEACHER
        if (role == Role.STUDENT) {
            Department department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

            Student student = new Student();
            student.setFullName(request.getUsername());
            student.setEnrollmentNumber(generateEnrollmentNumber()); // implement this
            student.setDepartment(department);
            student.setAuth(savedAuth);

            studentRepository.save(student);

        } else if (role == Role.TEACHER) {
            Department department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

            Teacher teacher = new Teacher();
            teacher.setFullName(request.getFullName());
            teacher.setDepartment(department);
            teacher.setAuth(savedAuth);

            teacherRepository.save(teacher);
        }

        // Generate JWT token
        JwtService.TokenWithExpiry tokenData = jwtService.generateTokenWithExpiry(
                savedAuth.getEmail(), role.name());

        return AuthResponseDTO.builder()
                .token(tokenData.token())
                .username(savedAuth.getUsername())
                .role(role.name())
                .userId(savedAuth.getId())
                .message("Registration successful")
                .expiresAt(tokenData.expiresAt())
                .build();
    }

    @Override
    public AuthResponseDTO loginUser(AuthRequestDTO request) {

        Auth user = authRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Password Does Not Match");
        }

        // Take first role for JWT
        Role role = user.getRoles().iterator().next();

        JwtService.TokenWithExpiry tokenData = jwtService.generateTokenWithExpiry(
                user.getEmail(), role.name());

        return AuthResponseDTO.builder()
                .token(tokenData.token())
                .username(user.getUsername())
                .role(role.name())
                .userId(user.getId())
                .message("Authentication successful")
                .expiresAt(tokenData.expiresAt())
                .build();
    }





    /**
     * Generate a unique enrollment number for a student.
     * You can implement your logic here, e.g., "STU0001", "STU0002" etc.
     */
    private String generateEnrollmentNumber() {
        long count = studentRepository.count() + 1;
        return String.format("STU%04d", count);
    }
}