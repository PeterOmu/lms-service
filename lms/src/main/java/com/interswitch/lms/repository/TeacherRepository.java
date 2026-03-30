//package com.interswitch.lms.repository;
//
//
//import com.interswitch.lms.entity.Teacher;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.List;
//
//public interface TeacherRepository extends JpaRepository<Teacher, Long> {
//
//    List<Teacher> findByDepartmentId(Long departmentId);
//}

package com.interswitch.lms.repository;

import com.interswitch.lms.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    // Existing method
    List<Teacher> findByDepartmentId(Long departmentId);

    // New method to fetch teacher by linked Auth email (used in seeder)
    Optional<Teacher> findByAuthEmail(String email);
}