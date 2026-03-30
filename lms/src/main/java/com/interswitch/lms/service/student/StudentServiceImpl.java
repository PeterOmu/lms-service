package com.interswitch.lms.service.student;

import com.interswitch.lms.dto.request.CreateStudentRequestDTO;
import com.interswitch.lms.dto.response.StudentResponseDTO;
import com.interswitch.lms.entity.Auth;
import com.interswitch.lms.entity.Department;
import com.interswitch.lms.entity.Student;
import com.interswitch.lms.exception.ResourceAlreadyExistsException;
import com.interswitch.lms.exception.ResourceNotFoundException;
import com.interswitch.lms.repository.AuthRepository;
import com.interswitch.lms.repository.DepartmentRepository;
import com.interswitch.lms.repository.StudentRepository;
import com.interswitch.lms.service.student.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public StudentResponseDTO createStudent(CreateStudentRequestDTO request) {

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Department not found with id: " + request.getDepartmentId()));

        if (studentRepository.existsByEnrollmentNumber(request.getEnrollmentNumber())) {
            throw new ResourceAlreadyExistsException(
                    "Student with enrollment number '" + request.getEnrollmentNumber() + "' already exists");
        }

        Auth auth = new Auth();
        auth.setEmail(request.getEnrollmentNumber() + "@student.lms.com");
        auth.setUsername(request.getEnrollmentNumber());
        auth.setPassword(passwordEncoder.encode("password123"));
        auth.setRoles(Set.of(Auth.Role.STUDENT));

        Auth savedAuth = authRepository.save(auth);

        Student student = new Student();
        student.setFullName(request.getFullName());
        student.setEnrollmentNumber(request.getEnrollmentNumber());
        student.setDepartment(department);
        student.setAuth(savedAuth);

        Student savedStudent = studentRepository.save(student);

        return mapToResponseDTO(savedStudent);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponseDTO getStudentById(Long id) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found with id: " + id));

        return mapToResponseDTO(student);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponseDTO> getAllStudents() {

        return studentRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponseDTO> getStudentsByDepartment(Long departmentId) {

        departmentRepository.findById(departmentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Department not found with id: " + departmentId));

        return studentRepository.findByDepartmentId(departmentId)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public StudentResponseDTO updateStudent(Long id, CreateStudentRequestDTO request) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found with id: " + id));

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Department not found with id: " + request.getDepartmentId()));

        if (!student.getEnrollmentNumber().equals(request.getEnrollmentNumber())
                && studentRepository.existsByEnrollmentNumber(request.getEnrollmentNumber())) {

            throw new ResourceAlreadyExistsException(
                    "Student with enrollment number '" + request.getEnrollmentNumber() + "' already exists");
        }

        student.setFullName(request.getFullName());
        student.setEnrollmentNumber(request.getEnrollmentNumber());
        student.setDepartment(department);

        Student updatedStudent = studentRepository.save(student);

        return mapToResponseDTO(updatedStudent);
    }

    @Override
    @Transactional
    public void deleteStudent(Long id) {

        if (!studentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Student not found with id: " + id);
        }

        studentRepository.deleteById(id);
    }

    private StudentResponseDTO mapToResponseDTO(Student student) {

        return StudentResponseDTO.builder()
                .id(student.getId())
                .fullName(student.getFullName())
                .enrollmentNumber(student.getEnrollmentNumber())
                .email(student.getAuth().getEmail())
                .departmentId(student.getDepartment().getId())
                .departmentName(student.getDepartment().getName())
                .createdAt(student.getAuth().getCreatedAt())
                .updatedAt(student.getAuth().getUpdatedAt())
                .build();
    }
}