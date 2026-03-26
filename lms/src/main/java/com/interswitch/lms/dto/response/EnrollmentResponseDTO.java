package com.interswitch.lms.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentResponseDTO {

    private Long id;

    private Long studentId;

    private String studentName;

    private Long courseId;

    private String courseTitle;

    private LocalDateTime enrolledAt;
}