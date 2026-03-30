package com.interswitch.lms.mapper;

import com.interswitch.lms.dto.request.CreateCourseRequestDTO;
import com.interswitch.lms.dto.response.CourseResponseDTO;
import com.interswitch.lms.entity.Course;
import com.interswitch.lms.entity.Department;
import com.interswitch.lms.entity.Teacher;

public class CourseMapper {

    // Map DTO to Entity
    public static Course toEntity(CreateCourseRequestDTO dto, Teacher teacher, Department department) {
        return Course.builder()
                .name(dto.getTitle())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .isPaid(dto.getIsPaid())
                .teacher(teacher)
                .department(department)
                .build();
    }

    // Map Entity to DTO
    public static CourseResponseDTO toDTO(Course course) {
        return CourseResponseDTO.builder()
                .id(course.getId())
                .title(course.getName())
                .description(course.getDescription())
                .teacherName(course.getTeacher().getFullName())
                .departmentName(course.getDepartment().getName())
                .url("/courses/" + course.getId())
                .createdAt(course.getCreatedAt())
                .updatedAt(course.getUpdatedAt())
                .build();
    }
}