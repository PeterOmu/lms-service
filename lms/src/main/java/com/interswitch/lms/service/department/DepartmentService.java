package com.interswitch.lms.service.department;

import com.interswitch.lms.dto.request.CreateDepartmentRequestDTO;
import com.interswitch.lms.dto.response.DepartmentResponseDTO;

import java.util.List;

public interface DepartmentService {

    /**
     * Create a new department
     */
    DepartmentResponseDTO createDepartment(CreateDepartmentRequestDTO request);

    /**
     * Get all departments
     */
    List<DepartmentResponseDTO> getAllDepartments();

    /**
     * Get department by ID
     */
    DepartmentResponseDTO getDepartmentById(Long id);

    /**
     * Update an existing department
     */
    DepartmentResponseDTO updateDepartment(Long id, CreateDepartmentRequestDTO request);

    /**
     * Delete a department
     */
    void deleteDepartment(Long id);
}
