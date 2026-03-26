package com.interswitch.lms.repository;

import com.interswitch.lms.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByEnrollmentNumber(String enrollmentNumber);

    List<Student> findByDepartmentId(Long departmentId);

    boolean existsByEnrollmentNumber(String enrollmentNumber);
}