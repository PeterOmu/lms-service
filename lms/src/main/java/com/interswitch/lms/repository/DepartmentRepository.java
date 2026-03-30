package com.interswitch.lms.repository;

import com.interswitch.lms.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    /**
     * Check if a department with the given name already exists
     */
    boolean existsByNameIgnoreCase(String name);

    /**
     * Find department by name
     */
    Optional<Department> findByNameIgnoreCase(String name);
}