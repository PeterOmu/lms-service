package com.interswitch.lms.service.student;

import com.interswitch.lms.dto.request.CreateStudentRequestDTO;
import com.interswitch.lms.dto.response.StudentResponseDTO;

import java.util.List;

public interface StudentService {

    StudentResponseDTO createStudent(CreateStudentRequestDTO request);

    StudentResponseDTO getStudentById(Long id);

    List<StudentResponseDTO> getAllStudents();

    List<StudentResponseDTO> getStudentsByDepartment(Long departmentId);

    StudentResponseDTO updateStudent(Long id, CreateStudentRequestDTO request);

    void deleteStudent(Long id);
}