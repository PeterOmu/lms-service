package com.interswitch.lms.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateCourseRequestDTO {

    @NotBlank(message = "Course title is required")
    @Size(max = 100)
    private String title;

    @Size(max = 255)
    private String description;

    @Min(value = 0, message = "Price must be zero or positive")
    private int price;

    @NotNull(message = "Teacher ID is required")
    private Long teacherId;

    @NotNull(message = "Department ID is required")
    private Long departmentId;

    @NotNull(message = "isPaid flag must be specified")
    private Boolean isPaid;
}