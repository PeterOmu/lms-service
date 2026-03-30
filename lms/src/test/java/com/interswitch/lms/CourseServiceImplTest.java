package com.interswitch.lms;

import com.interswitch.lms.dto.request.CreateCourseRequestDTO;
import com.interswitch.lms.dto.response.CourseResponseDTO;
import com.interswitch.lms.entity.Course;
import com.interswitch.lms.entity.Department;
import com.interswitch.lms.entity.Teacher;
import com.interswitch.lms.repository.CourseRepository;
import com.interswitch.lms.repository.DepartmentRepository;
import com.interswitch.lms.repository.TeacherRepository;
import com.interswitch.lms.service.course.CourseServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {

    @Mock
    private CourseRepository courseRepository;
    @Mock
    private TeacherRepository teacherRepository;
    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private CourseServiceImpl courseService;


@Test
void createCourse_Success() {

    Teacher teacher = new Teacher();
    teacher.setId(1L);

    Department department = new Department();
    department.setId(1L);
    department.setName("Science");

    CreateCourseRequestDTO request = CreateCourseRequestDTO.builder()
            .title("Physics")
            .description("Intro Physics")
            .price(0)
            .teacherId(1L)
            .departmentId(1L)
            .isPaid(false)
            .build();

    when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));
    when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));

    when(courseRepository.save(any(Course.class))).thenAnswer(invocation -> {
        Course c = invocation.getArgument(0);
        c.setId(1L);
        return c;
    });

    CourseResponseDTO response = courseService.createCourse(request);

    assertEquals(1L, response.getId());
    assertEquals("Physics", response.getTitle());
    assertEquals("Science", response.getDepartmentName());
}

    @Test
    void createCourse_TeacherNotFound() {
        CreateCourseRequestDTO request = new CreateCourseRequestDTO();
        request.setTeacherId(1L);
        request.setDepartmentId(1L);

        when(teacherRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> courseService.createCourse(request));
    }

    @Test
    void getCourseById_Success() {

        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFullName("Dr John");

        Department department = new Department();
        department.setId(1L);
        department.setName("Science");

        Course course = new Course();
        course.setId(1L);
        course.setName("Physics");
        course.setTeacher(teacher);
        course.setDepartment(department);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        CourseResponseDTO response = courseService.getCourseById(1L);

        assertEquals("Physics", response.getTitle());
        assertEquals("Science", response.getDepartmentName());
        assertEquals("Dr John", response.getTeacherName());
    }

    @Test
    void deleteCourse_Success() {
        when(courseRepository.existsById(1L)).thenReturn(true);
        courseService.deleteCourse(1L);
        verify(courseRepository).deleteById(1L);
    }

    @Test
    void deleteCourse_NotFound() {
        when(courseRepository.existsById(1L)).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> courseService.deleteCourse(1L));
    }
}