package com.interswitch.lms.controller;

import com.interswitch.lms.dto.request.CreateStudentRequestDTO;
import com.interswitch.lms.dto.response.StudentResponseDTO;
import com.interswitch.lms.service.student.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<StudentResponseDTO> createStudent(
            @RequestBody CreateStudentRequestDTO request) {

        StudentResponseDTO student = studentService.createStudent(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(student);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> getStudentById(@PathVariable Long id) {

        StudentResponseDTO student = studentService.getStudentById(id);

        return ResponseEntity.ok(student);
    }

    @GetMapping
    public ResponseEntity<List<StudentResponseDTO>> getAllStudents() {

        List<StudentResponseDTO> students = studentService.getAllStudents();

        return ResponseEntity.ok(students);
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<StudentResponseDTO>> getStudentsByDepartment(
            @PathVariable Long departmentId) {

        List<StudentResponseDTO> students =
                studentService.getStudentsByDepartment(departmentId);

        return ResponseEntity.ok(students);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> updateStudent(
            @PathVariable Long id,
            @RequestBody CreateStudentRequestDTO request) {

        StudentResponseDTO student =
                studentService.updateStudent(id, request);

        return ResponseEntity.ok(student);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {

        studentService.deleteStudent(id);

        return ResponseEntity.noContent().build();
    }
}