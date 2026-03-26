package com.interswitch.lms.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeacherResponseDTO {

    private Long id;

    private String fullName;

    private String email;

    private String departmentName;

    private String designation;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}