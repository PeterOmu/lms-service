package com.interswitch.lms.dto.response;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseResponseDTO {

    private Long id;

    private String title;

    private String description;

    private String teacherName;

    private String url;

    private String departmentName;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}