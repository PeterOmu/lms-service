package com.interswitch.lms.mapper;

import com.interswitch.lms.dto.response.EnrollmentResponseDTO;
import com.interswitch.lms.entity.Enrollment;

public class EnrollmentMapper {

    public static EnrollmentResponseDTO toDTO(Enrollment enrollment) {
        return EnrollmentResponseDTO.builder()
                .id(enrollment.getId())
                .studentId(enrollment.getStudent().getId())
                .studentName(enrollment.getStudent().getFullName())
                .courseId(enrollment.getCourse().getId())
                .courseTitle(enrollment.getCourse().getName())
                .enrolledAt(enrollment.getEnrolledAt())
                .build();
    }
}