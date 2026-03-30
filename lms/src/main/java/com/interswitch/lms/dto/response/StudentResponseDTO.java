//package com.interswitch.lms.dto.response;
//
//import lombok.*;
//
//import java.time.LocalDateTime;
//
//@Getter
//@Setter
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
//public class StudentResponseDTO {
//
//    private Long id;
//
//    private String fullName;
//
//    private String enrollmentNumber;
//
//    private String email;
//
//    private String departmentName;
//
//    private String departmentId;
//
//    private LocalDateTime createdAt;
//
//    private LocalDateTime updatedAt;
//}

package com.interswitch.lms.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentResponseDTO {

    private Long id;

    private String fullName;

    private String enrollmentNumber;

    private String email;

    private Long departmentId;

    private String departmentName;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}