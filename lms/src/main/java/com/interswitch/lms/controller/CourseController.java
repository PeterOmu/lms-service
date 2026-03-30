
package com.interswitch.lms.controller;

import com.interswitch.lms.dto.request.CreateCourseRequestDTO;
import com.interswitch.lms.dto.response.CourseResponseDTO;
import com.interswitch.lms.service.course.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    // Create a new course
    @PostMapping
    public ResponseEntity<CourseResponseDTO> createCourse(
            @Valid @RequestBody CreateCourseRequestDTO createCourseRequestDTO) {
        CourseResponseDTO createdCourse = courseService.createCourse(createCourseRequestDTO);
        return new ResponseEntity<>(createdCourse, HttpStatus.CREATED);
    }

    // Get all courses
    @GetMapping
    public ResponseEntity<List<CourseResponseDTO>> getAllCourses() {
        List<CourseResponseDTO> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }

    // Get a course by ID
    @GetMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> getCourseById(@PathVariable Long id) {
        CourseResponseDTO course = courseService.getCourseById(id);
        return ResponseEntity.ok(course);
    }

    // Get courses by teacher ID
    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<CourseResponseDTO>> getCoursesByTeacher(@PathVariable Long teacherId) {
        List<CourseResponseDTO> courses = courseService.getCoursesByTeacher(teacherId);
        return ResponseEntity.ok(courses);
    }

    // Get courses by department ID
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<CourseResponseDTO>> getCoursesByDepartment(@PathVariable Long departmentId) {
        List<CourseResponseDTO> courses = courseService.getCoursesByDepartment(departmentId);
        return ResponseEntity.ok(courses);
    }

    // Delete a course by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}