package com.interswitch.lms.service.enrollment;

import com.interswitch.lms.dto.request.EnrollStudentRequestDTO;
import com.interswitch.lms.dto.response.EnrollmentResponseDTO;
import com.interswitch.lms.entity.Course;
import com.interswitch.lms.entity.Enrollment;
import com.interswitch.lms.entity.Student;
import com.interswitch.lms.mapper.EnrollmentMapper;
import com.interswitch.lms.repository.CourseRepository;
import com.interswitch.lms.repository.EnrollmentRepository;
import com.interswitch.lms.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    @Override
    public EnrollmentResponseDTO enrollStudent(EnrollStudentRequestDTO dto) {
        // Check if student exists
        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new EntityNotFoundException("Student not found with ID: " + dto.getStudentId()));

        // Check if course exists
        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new EntityNotFoundException("Course not found with ID: " + dto.getCourseId()));

        // Prevent duplicate enrollment
        if (enrollmentRepository.existsByStudentIdAndCourseId(dto.getStudentId(), dto.getCourseId())) {
            throw new IllegalArgumentException("Student is already enrolled in this course");
        }

        // Create and save enrollment
        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

        return EnrollmentMapper.toDTO(savedEnrollment);
    }

    @Override
    public List<EnrollmentResponseDTO> getEnrollmentsByStudent(Long studentId) {
        return enrollmentRepository.findByStudentId(studentId).stream()
                .map(EnrollmentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EnrollmentResponseDTO> getEnrollmentsByCourse(Long courseId) {
        return enrollmentRepository.findByCourseId(courseId).stream()
                .map(EnrollmentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteEnrollment(Long enrollmentId) {
        if (!enrollmentRepository.existsById(enrollmentId)) {
            throw new EntityNotFoundException("Enrollment not found with ID: " + enrollmentId);
        }
        enrollmentRepository.deleteById(enrollmentId);
    }
}