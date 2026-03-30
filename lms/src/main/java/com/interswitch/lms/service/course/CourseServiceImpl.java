package com.interswitch.lms.service.course;

import com.interswitch.lms.dto.request.CreateCourseRequestDTO;
import com.interswitch.lms.dto.response.CourseResponseDTO;
import com.interswitch.lms.entity.Course;
import com.interswitch.lms.entity.Department;
import com.interswitch.lms.entity.Teacher;
import com.interswitch.lms.mapper.CourseMapper;
import com.interswitch.lms.repository.CourseRepository;
import com.interswitch.lms.repository.DepartmentRepository;
import com.interswitch.lms.repository.TeacherRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final DepartmentRepository departmentRepository;

    @Override
    public CourseResponseDTO createCourse(CreateCourseRequestDTO dto) {
        Teacher teacher = teacherRepository.findById(dto.getTeacherId())
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found with ID: " + dto.getTeacherId()));

        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new EntityNotFoundException("Department not found with ID: " + dto.getDepartmentId()));

        Course course = CourseMapper.toEntity(dto, teacher, department);
        Course savedCourse = courseRepository.save(course);

        return CourseMapper.toDTO(savedCourse);
    }

    @Override
    public List<CourseResponseDTO> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(CourseMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CourseResponseDTO getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with ID: " + id));
        return CourseMapper.toDTO(course);
    }

    @Override
    public List<CourseResponseDTO> getCoursesByTeacher(Long teacherId) {
        return courseRepository.findByTeacherId(teacherId).stream()
                .map(CourseMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseResponseDTO> getCoursesByDepartment(Long departmentId) {
        return courseRepository.findByDepartmentId(departmentId).stream()
                .map(CourseMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new EntityNotFoundException("Course not found with ID: " + id);
        }
        courseRepository.deleteById(id);
    }
}