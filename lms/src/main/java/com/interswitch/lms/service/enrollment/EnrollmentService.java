
package com.interswitch.lms.service.enrollment;

import com.interswitch.lms.dto.request.EnrollStudentRequestDTO;
import com.interswitch.lms.dto.response.EnrollmentResponseDTO;

import java.util.List;

public interface EnrollmentService {

    EnrollmentResponseDTO enrollStudent(EnrollStudentRequestDTO dto);

    List<EnrollmentResponseDTO> getEnrollmentsByStudent(Long studentId);

    List<EnrollmentResponseDTO> getEnrollmentsByCourse(Long courseId);

    void deleteEnrollment(Long enrollmentId);
}