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
public class CreateTeacherRequestDTO {

    @NotBlank(message = "Full name is required")
    @Size(max = 100)
    private String fullName;

    @NotNull(message = "Department ID is required")
    private Long departmentId;

    @NotNull(message = "Designation is required")
    private String designation;
}