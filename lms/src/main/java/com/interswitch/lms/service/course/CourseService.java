package com.interswitch.lms.service.course;

import com.interswitch.lms.dto.request.CreateCourseRequestDTO;
import com.interswitch.lms.dto.response.CourseResponseDTO;

import java.util.List;

public interface CourseService {

    CourseResponseDTO createCourse(CreateCourseRequestDTO dto);

    List<CourseResponseDTO> getAllCourses();

    CourseResponseDTO getCourseById(Long id);

    List<CourseResponseDTO> getCoursesByTeacher(Long teacherId);

    List<CourseResponseDTO> getCoursesByDepartment(Long departmentId);

    void deleteCourse(Long id);
}