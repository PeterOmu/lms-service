package com.interswitch.lms.controller;

import com.interswitch.lms.dto.request.EnrollStudentRequestDTO;
import com.interswitch.lms.dto.response.EnrollmentResponseDTO;
import com.interswitch.lms.service.enrollment.EnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    // Enroll a student in a course
    @PostMapping
    public ResponseEntity<EnrollmentResponseDTO> enrollStudent(
            @Valid @RequestBody EnrollStudentRequestDTO requestDTO) {
        EnrollmentResponseDTO enrollment = enrollmentService.enrollStudent(requestDTO);
        return new ResponseEntity<>(enrollment, HttpStatus.CREATED);
    }

    // Get enrollments by student ID
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<EnrollmentResponseDTO>> getEnrollmentsByStudent(
            @PathVariable Long studentId) {
        List<EnrollmentResponseDTO> enrollments = enrollmentService.getEnrollmentsByStudent(studentId);
        return ResponseEntity.ok(enrollments);
    }

    // Get enrollments by course ID
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<EnrollmentResponseDTO>> getEnrollmentsByCourse(
            @PathVariable Long courseId) {
        List<EnrollmentResponseDTO> enrollments = enrollmentService.getEnrollmentsByCourse(courseId);
        return ResponseEntity.ok(enrollments);
    }

    // Delete an enrollment
    @DeleteMapping("/{enrollmentId}")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable Long enrollmentId) {
        enrollmentService.deleteEnrollment(enrollmentId);
        return ResponseEntity.noContent().build();
    }
}