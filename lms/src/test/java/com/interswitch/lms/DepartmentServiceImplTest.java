package com.interswitch.lms;

import com.interswitch.lms.dto.request.CreateDepartmentRequestDTO;
import com.interswitch.lms.dto.response.DepartmentResponseDTO;
import com.interswitch.lms.entity.Department;
import com.interswitch.lms.exception.ResourceAlreadyExistsException;
import com.interswitch.lms.exception.ResourceNotFoundException;
import com.interswitch.lms.repository.DepartmentRepository;
import com.interswitch.lms.service.department.DepartmentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceImplTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    @Test
    void createDepartment_Success() {
        CreateDepartmentRequestDTO request = new CreateDepartmentRequestDTO();
        request.setName("Science");
        request.setDescription("Science Department");

        Department dept = new Department();
        dept.setId(1L);
        dept.setName("Science");
        dept.setDescription("Science Department");

        when(departmentRepository.existsByNameIgnoreCase("Science")).thenReturn(false);
        when(departmentRepository.save(any(Department.class))).thenReturn(dept);

        DepartmentResponseDTO response = departmentService.createDepartment(request);

        assertEquals(1L, response.getId());
        assertEquals("Science", response.getName());
        verify(departmentRepository).save(any(Department.class));
    }

    @Test
    void createDepartment_ThrowsDuplicateName() {
        CreateDepartmentRequestDTO request = new CreateDepartmentRequestDTO();
        request.setName("Science");

        when(departmentRepository.existsByNameIgnoreCase("Science")).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () ->
                departmentService.createDepartment(request));
    }

    @Test
    void getDepartmentById_Success() {
        Department dept = new Department();
        dept.setId(1L);
        dept.setName("Math");

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(dept));

        DepartmentResponseDTO response = departmentService.getDepartmentById(1L);
        assertEquals("Math", response.getName());
    }

    @Test
    void getDepartmentById_NotFound() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () ->
                departmentService.getDepartmentById(1L));
    }

    @Test
    void updateDepartment_Success() {
        Department dept = new Department();
        dept.setId(1L);
        dept.setName("Math");

        CreateDepartmentRequestDTO request = new CreateDepartmentRequestDTO();
        request.setName("Mathematics");

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(dept));
        when(departmentRepository.existsByNameIgnoreCase("Mathematics")).thenReturn(false);
        when(departmentRepository.save(any(Department.class))).thenAnswer(i -> i.getArgument(0));

        DepartmentResponseDTO response = departmentService.updateDepartment(1L, request);

        assertEquals("Mathematics", response.getName());
    }

    @Test
    void deleteDepartment_Success() {
        when(departmentRepository.existsById(1L)).thenReturn(true);
        departmentService.deleteDepartment(1L);
        verify(departmentRepository).deleteById(1L);
    }

    @Test
    void deleteDepartment_NotFound() {
        when(departmentRepository.existsById(1L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () ->
                departmentService.deleteDepartment(1L));
    }
}