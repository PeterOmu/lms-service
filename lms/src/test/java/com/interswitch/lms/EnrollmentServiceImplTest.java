package com.interswitch.lms;

import com.interswitch.lms.dto.request.EnrollStudentRequestDTO;
import com.interswitch.lms.dto.response.EnrollmentResponseDTO;
import com.interswitch.lms.entity.Course;
import com.interswitch.lms.entity.Enrollment;
import com.interswitch.lms.entity.Student;
import com.interswitch.lms.repository.CourseRepository;
import com.interswitch.lms.repository.EnrollmentRepository;
import com.interswitch.lms.repository.StudentRepository;
import com.interswitch.lms.service.enrollment.EnrollmentServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnrollmentServiceImplTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private EnrollmentServiceImpl enrollmentService;

    @Test
    void enrollStudent_Success() {
        Student student = new Student();
        student.setId(1L);

        Course course = new Course();
        course.setId(1L);

        EnrollStudentRequestDTO request = new EnrollStudentRequestDTO();
        request.setStudentId(1L);
        request.setCourseId(1L);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(enrollmentRepository.existsByStudentIdAndCourseId(1L, 1L)).thenReturn(false);
        when(enrollmentRepository.save(any(Enrollment.class))).thenAnswer(i -> {
            Enrollment e = i.getArgument(0);
            e.setId(1L);
            return e;
        });

        EnrollmentResponseDTO response = enrollmentService.enrollStudent(request);
        assertEquals(1L, response.getId());
    }

    @Test
    void enrollStudent_AlreadyEnrolled() {
        EnrollStudentRequestDTO request = new EnrollStudentRequestDTO();
        request.setStudentId(1L);
        request.setCourseId(1L);

        when(enrollmentRepository.existsByStudentIdAndCourseId(1L, 1L)).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () ->
                enrollmentService.enrollStudent(request));
    }

    @Test
    void deleteEnrollment_Success() {
        when(enrollmentRepository.existsById(1L)).thenReturn(true);
        enrollmentService.deleteEnrollment(1L);
        verify(enrollmentRepository).deleteById(1L);
    }

    @Test
    void deleteEnrollment_NotFound() {
        when(enrollmentRepository.existsById(1L)).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () ->
                enrollmentService.deleteEnrollment(1L));
    }
}