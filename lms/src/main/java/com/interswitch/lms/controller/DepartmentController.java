//package com.interswitch.lms.controller;
//
//import com.interswitch.lms.dto.request.CreateDepartmentRequestDTO;
//import com.interswitch.lms.dto.response.ApiResponse;
//import com.interswitch.lms.dto.response.DepartmentResponseDTO;
//import com.interswitch.lms.service.department.DepartmentService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/v1/departments")
//@RequiredArgsConstructor
//public class DepartmentController {
//
//    private final DepartmentService departmentService;
//
//    /**
//     * Create a new Department
//     * POST /api/v1/departments
//     */
//    @PostMapping
//    public ResponseEntity<ApiResponse<DepartmentResponseDTO>> createDepartment(
//            @Valid @RequestBody CreateDepartmentRequestDTO request) {
//
//        DepartmentResponseDTO response = departmentService.createDepartment(request);
//
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(ApiResponse.success("Department created successfully", response));
//    }
//
//    /**
//     * Get All Departments
//     * GET /api/v1/departments
//     */
//    @GetMapping
//    public ResponseEntity<ApiResponse<List<DepartmentResponseDTO>>> getAllDepartments() {
//
//        List<DepartmentResponseDTO> departments = departmentService.getAllDepartments();
//
//        return ResponseEntity.ok(
//                ApiResponse.success("Departments retrieved successfully", departments)
//        );
//    }
//
//    /**
//     * Get Department by ID
//     * GET /api/v1/departments/{id}
//     */
//    @GetMapping("/{id}")
//    public ResponseEntity<ApiResponse<DepartmentResponseDTO>> getDepartmentById(@PathVariable Long id) {
//
//        DepartmentResponseDTO department = departmentService.getDepartmentById(id);
//
//        return ResponseEntity.ok(
//                ApiResponse.success("Department retrieved successfully", department)
//        );
//    }
//
//    /**
//     * Update Department
//     * PUT /api/v1/departments/{id}
//     */
//    @PutMapping("/{id}")
//    public ResponseEntity<ApiResponse<DepartmentResponseDTO>> updateDepartment(
//            @PathVariable Long id,
//            @Valid @RequestBody CreateDepartmentRequestDTO request) {
//
//        DepartmentResponseDTO updatedDepartment = departmentService.updateDepartment(id, request);
//
//        return ResponseEntity.ok(
//                ApiResponse.success("Department updated successfully", updatedDepartment)
//        );
//    }
//
//    /**
//     * Delete Department
//     * DELETE /api/v1/departments/{id}
//     */
//    @DeleteMapping("/{id}")
//    public ResponseEntity<ApiResponse<String>> deleteDepartment(@PathVariable Long id) {
//
//        departmentService.deleteDepartment(id);
//
//        return ResponseEntity.ok(
//                ApiResponse.success("Department deleted successfully", null)
//        );
//    }
//}


package com.interswitch.lms.controller;

import com.interswitch.lms.dto.request.CreateDepartmentRequestDTO;
import com.interswitch.lms.dto.response.ApiResponse;
import com.interswitch.lms.dto.response.DepartmentResponseDTO;
import com.interswitch.lms.service.department.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    /**
     * Only ADMIN can create department
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<DepartmentResponseDTO>> createDepartment(
            @Valid @RequestBody CreateDepartmentRequestDTO request) {

        DepartmentResponseDTO response = departmentService.createDepartment(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Department created successfully", response));
    }

    /**
     * Anyone authenticated can view departments (Student, Teacher, Admin)
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<DepartmentResponseDTO>>> getAllDepartments() {
        List<DepartmentResponseDTO> departments = departmentService.getAllDepartments();

        return ResponseEntity.ok(
                ApiResponse.success("Departments retrieved successfully", departments)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentResponseDTO>> getDepartmentById(@PathVariable Long id) {
        DepartmentResponseDTO department = departmentService.getDepartmentById(id);

        return ResponseEntity.ok(
                ApiResponse.success("Department retrieved successfully", department)
        );
    }

    /**
     * Only ADMIN can update department
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentResponseDTO>> updateDepartment(
            @PathVariable Long id,
            @Valid @RequestBody CreateDepartmentRequestDTO request) {

        DepartmentResponseDTO updated = departmentService.updateDepartment(id, request);

        return ResponseEntity.ok(
                ApiResponse.success("Department updated successfully", updated)
        );
    }

    /**
     * Only ADMIN can delete department
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);

        return ResponseEntity.ok(
                ApiResponse.success("Department deleted successfully", null)
        );
    }
}