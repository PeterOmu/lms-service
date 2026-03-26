package com.interswitch.lms.repository;


import com.interswitch.lms.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    List<Teacher> findByDepartmentId(Long departmentId);
}