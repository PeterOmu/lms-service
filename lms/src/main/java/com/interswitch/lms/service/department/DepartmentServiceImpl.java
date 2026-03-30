package com.interswitch.lms.service.department;

import com.interswitch.lms.dto.request.CreateDepartmentRequestDTO;
import com.interswitch.lms.dto.response.DepartmentResponseDTO;
import com.interswitch.lms.entity.Department;
import com.interswitch.lms.exception.ResourceAlreadyExistsException;
import com.interswitch.lms.exception.ResourceNotFoundException;
import com.interswitch.lms.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Override
    @Transactional
    public DepartmentResponseDTO createDepartment(CreateDepartmentRequestDTO request) {
        // Check for duplicate department name
        if (departmentRepository.existsByNameIgnoreCase(request.getName())) {
            throw new ResourceAlreadyExistsException(
                    "Department with name '" + request.getName() + "' already exists");
        }

        Department department = new Department();
        department.setName(request.getName().trim());
        department.setDescription(request.getDescription() != null ? request.getDescription().trim() : null);

        Department savedDepartment = departmentRepository.save(department);

        return mapToResponseDTO(savedDepartment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentResponseDTO> getAllDepartments() {
        return departmentRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentResponseDTO getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Department not found with id: " + id));

        return mapToResponseDTO(department);
    }

    @Override
    @Transactional
    public DepartmentResponseDTO updateDepartment(Long id, CreateDepartmentRequestDTO request) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Department not found with id: " + id));

        // Check for duplicate name (excluding current department)
        if (!department.getName().equalsIgnoreCase(request.getName()) &&
                departmentRepository.existsByNameIgnoreCase(request.getName())) {
            throw new ResourceAlreadyExistsException(
                    "Department with name '" + request.getName() + "' already exists");
        }

        department.setName(request.getName().trim());
        department.setDescription(request.getDescription() != null ? request.getDescription().trim() : null);

        Department updatedDepartment = departmentRepository.save(department);

        return mapToResponseDTO(updatedDepartment);
    }

    @Override
    @Transactional
    public void deleteDepartment(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Department not found with id: " + id);
        }

        // TODO: Add business check - prevent deletion if department has students or courses
        departmentRepository.deleteById(id);
    }

    /**
     * Helper method to map Department entity to DepartmentResponseDTO
     */
    private DepartmentResponseDTO mapToResponseDTO(Department department) {
        return DepartmentResponseDTO.builder()
                .id(department.getId())
                .name(department.getName())
                .description(department.getDescription())
                .createdAt(department.getCreatedAt())
                .updatedAt(department.getUpdatedAt())
                .build();
    }
}