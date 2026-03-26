package com.interswitch.lms.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateStudentRequestDTO {

    @NotBlank(message = "Full name is required")
    @Size(max = 100)
    private String fullName;

    @NotBlank(message = "Enrollment number is required")
    @Size(max = 50)
    private String enrollmentNumber;

    @NotNull(message = "Department ID is required")
    private Long departmentId;
}