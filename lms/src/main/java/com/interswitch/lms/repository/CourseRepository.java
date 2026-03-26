package com.interswitch.lms.repository;


import com.interswitch.lms.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByTeacherId(Long teacherId);

    List<Course> findByDepartmentId(Long departmentId);
}