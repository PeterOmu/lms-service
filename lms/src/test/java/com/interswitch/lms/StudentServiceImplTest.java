package com.interswitch.lms;

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
import com.interswitch.lms.service.student.StudentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceImplTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private AuthRepository authRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private StudentServiceImpl studentService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createStudent_Success() {
        CreateStudentRequestDTO request = new CreateStudentRequestDTO();
        request.setFullName("Alice Johnson");
        request.setEnrollmentNumber("ENR123");
        request.setDepartmentId(1L);

        Department dept = new Department();
        dept.setId(1L);
        dept.setName("Computer Science");

        Auth auth = new Auth();
        auth.setId(10L);
        auth.setEmail("ENR123@student.lms.com");
        auth.setUsername("ENR123");
        auth.setRoles(Set.of(Auth.Role.STUDENT));
        auth.setCreatedAt(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()));
        auth.setUpdatedAt(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()));

        Student student = new Student();
        student.setId(100L);
        student.setFullName(request.getFullName());
        student.setEnrollmentNumber(request.getEnrollmentNumber());
        student.setDepartment(dept);
        student.setAuth(auth);

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(dept));
        when(studentRepository.existsByEnrollmentNumber("ENR123")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("hashedPassword");
        when(authRepository.save(any(Auth.class))).thenReturn(auth);
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        StudentResponseDTO response = studentService.createStudent(request);

        assertNotNull(response);
        assertEquals("ENR123", response.getEnrollmentNumber());
        assertEquals("Alice Johnson", response.getFullName());
        assertEquals("Computer Science", response.getDepartmentName());
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void createStudent_ThrowsDuplicateEnrollment() {

        CreateStudentRequestDTO dto = CreateStudentRequestDTO.builder()
                .fullName("John Doe")
                .enrollmentNumber("ENR123")
                .departmentId(1L)
                .build();

        Department department = new Department();
        department.setId(1L);
        department.setName("Computer Science");

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));

        when(studentRepository.existsByEnrollmentNumber(dto.getEnrollmentNumber())).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> studentService.createStudent(dto));
    }

    @Test
    void createStudent_ThrowsDepartmentNotFound() {
        CreateStudentRequestDTO request = new CreateStudentRequestDTO();
        request.setDepartmentId(99L);

        when(departmentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.createStudent(request));
    }

    @Test
    void getStudentById_Success() {
        Student student = new Student();
        student.setId(1L);
        student.setFullName("Alice");
        student.setEnrollmentNumber("ENR123");
        Department dept = new Department();
        dept.setId(1L);
        dept.setName("CS");
        student.setDepartment(dept);
        Auth auth = new Auth();
        auth.setEmail("ENR123@student.lms.com");
        auth.setCreatedAt(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()));
        auth.setUpdatedAt(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()));
        student.setAuth(auth);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        StudentResponseDTO dto = studentService.getStudentById(1L);

        assertNotNull(dto);
        assertEquals("ENR123", dto.getEnrollmentNumber());
    }

    @Test
    void getStudentById_ThrowsNotFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.getStudentById(1L));
    }

    @Test
    void getAllStudents_ReturnsList() {
        Student student = new Student();
        student.setId(1L);
        student.setFullName("Alice");
        student.setEnrollmentNumber("ENR123");
        Department dept = new Department();
        dept.setId(1L);
        dept.setName("CS");
        student.setDepartment(dept);
        Auth auth = new Auth();
        auth.setEmail("ENR123@student.lms.com");
        auth.setCreatedAt(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()));
        auth.setUpdatedAt(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()));
        student.setAuth(auth);

        when(studentRepository.findAll()).thenReturn(List.of(student));

        List<StudentResponseDTO> list = studentService.getAllStudents();
        assertEquals(1, list.size());
        assertEquals("ENR123", list.get(0).getEnrollmentNumber());
    }

    @Test
    void getStudentsByDepartment_Success() {
        Department dept = new Department();
        dept.setId(1L);
        dept.setName("CS");

        Student student = new Student();
        student.setId(1L);
        student.setFullName("Alice");
        student.setEnrollmentNumber("ENR123");
        student.setDepartment(dept);
        Auth auth = new Auth();
        auth.setEmail("ENR123@student.lms.com");
        auth.setCreatedAt(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()));
        auth.setUpdatedAt(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()));
        student.setAuth(auth);

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(dept));
        when(studentRepository.findByDepartmentId(1L)).thenReturn(List.of(student));

        List<StudentResponseDTO> list = studentService.getStudentsByDepartment(1L);
        assertEquals(1, list.size());
    }

    @Test
    void getStudentsByDepartment_ThrowsDepartmentNotFound() {
        when(departmentRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> studentService.getStudentsByDepartment(99L));
    }

    @Test
    void updateStudent_Success() {
        Department dept = new Department();
        dept.setId(1L);
        dept.setName("CS");

        Auth auth = new Auth();
        auth.setEmail("alice@example.com");
        auth.setId(1L);

        Student student = new Student();
        student.setId(1L);
        student.setFullName("Alice");
        student.setEnrollmentNumber("ENR123");
        student.setDepartment(dept);
        student.setAuth(auth);  // <-- must set auth to avoid NPE

        CreateStudentRequestDTO request = new CreateStudentRequestDTO();
        request.setFullName("Alice Updated");
        request.setEnrollmentNumber("ENR123");
        request.setDepartmentId(1L);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(dept));
        when(studentRepository.existsByEnrollmentNumber("ENR123")).thenReturn(false);
        when(studentRepository.save(any(Student.class))).thenAnswer(i -> i.getArgument(0));

        StudentResponseDTO dto = studentService.updateStudent(1L, request);

        assertEquals("Alice Updated", dto.getFullName());
        assertEquals("alice@example.com", dto.getEmail()); // optional check
    }

    @Test
    void updateStudent_ThrowsDuplicateEnrollment() {
        Student student = new Student();
        student.setId(1L);
        student.setEnrollmentNumber("ENR123");

        CreateStudentRequestDTO request = new CreateStudentRequestDTO();
        request.setEnrollmentNumber("ENR456");
        request.setDepartmentId(1L);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(new Department()));
        when(studentRepository.existsByEnrollmentNumber("ENR456")).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> studentService.updateStudent(1L, request));
    }

    @Test
    void deleteStudent_Success() {
        when(studentRepository.existsById(1L)).thenReturn(true);
        doNothing().when(studentRepository).deleteById(1L);

        assertDoesNotThrow(() -> studentService.deleteStudent(1L));
        verify(studentRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteStudent_ThrowsNotFound() {
        when(studentRepository.existsById(1L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> studentService.deleteStudent(1L));
    }
}
