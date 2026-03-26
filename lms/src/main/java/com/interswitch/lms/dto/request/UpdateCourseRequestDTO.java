package com.interswitch.lms.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCourseRequestDTO {

    @NotBlank(message = "Course title is required")
    @Size(max = 100)
    private String title;

    @Size(max = 255)
    private String description;
}