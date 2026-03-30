package com.interswitch.lms.config;

import com.interswitch.lms.dto.request.RegisterRequestDTO;
import com.interswitch.lms.entity.Course;
import com.interswitch.lms.entity.Department;
import com.interswitch.lms.entity.Teacher;
import com.interswitch.lms.entity.Auth.Role;
import com.interswitch.lms.repository.CourseRepository;
import com.interswitch.lms.repository.DepartmentRepository;
import com.interswitch.lms.repository.StudentRepository;
import com.interswitch.lms.repository.TeacherRepository;
import com.interswitch.lms.service.auth.AuthService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedDatabase(StudentRepository studentRepository,
                                   TeacherRepository teacherRepository,
                                   DepartmentRepository departmentRepository,
                                   CourseRepository courseRepository,
                                   AuthService authService) {
        return args -> {

            // --- Skip if any data exists ---
            if (studentRepository.count() > 0 || teacherRepository.count() > 0 ||
                    departmentRepository.count() > 0 || courseRepository.count() > 0) {
                System.out.println("Data already exists, skipping seeding.");
                return;
            }

            // --- Departments ---
            Department d1 = new Department();
            d1.setName("Computer Science");
            Department d2 = new Department();
            d2.setName("Mathematics");
            Department d3 = new Department();
            d3.setName("Physics");

            departmentRepository.saveAll(List.of(d1, d2, d3));
            System.out.println("Seeded 3 departments.");


            RegisterRequestDTO adminUser = RegisterRequestDTO.builder()
                    .username("Admin")
                    .email("admin@example.com")
                    .password("password123")
                    .fullName("Admin User")
                    .role(Role.ADMIN)
                    .build();

            // --- Teachers ---
            RegisterRequestDTO teacher1DTO = RegisterRequestDTO.builder()
                    .username("johndoe")
                    .email("john@example.com")
                    .password("password123")
                    .fullName("John Doe")
                    .departmentId(d1.getId())
                    .role(Role.TEACHER)
                    .build();

            RegisterRequestDTO teacher2DTO = RegisterRequestDTO.builder()
                    .username("janesmith")
                    .email("jane@example.com")
                    .password("password123")
                    .fullName("Jane Smith")
                    .departmentId(d2.getId())
                    .role(Role.TEACHER)
                    .build();

            RegisterRequestDTO teacher3DTO = RegisterRequestDTO.builder()
                    .username("mikejohnson")
                    .email("mike@example.com")
                    .password("password123")
                    .fullName("Mike Johnson")
                    .departmentId(d3.getId())
                    .role(Role.TEACHER)
                    .build();

            authService.register(adminUser);
            authService.register(teacher1DTO);
            authService.register(teacher2DTO);
            authService.register(teacher3DTO);
            System.out.println("Seeded 3 teachers.");

            // Fetch teacher entities to link courses
            Teacher t1 = teacherRepository.findByAuthEmail("john@example.com")
                    .orElseThrow();
            Teacher t2 = teacherRepository.findByAuthEmail("jane@example.com")
                    .orElseThrow();
            Teacher t3 = teacherRepository.findByAuthEmail("mike@example.com")
                    .orElseThrow();

            // --- Students ---
            RegisterRequestDTO student1DTO = RegisterRequestDTO.builder()
                    .username("alice")
                    .email("alice@example.com")
                    .password("password123")
                    .fullName("Alice Johnson")
                    .departmentId(d1.getId())
                    .role(Role.STUDENT)
                    .build();

            RegisterRequestDTO student2DTO = RegisterRequestDTO.builder()
                    .username("bob")
                    .email("bob@example.com")
                    .password("password123")
                    .fullName("Bob Smith")
                    .departmentId(d2.getId())
                    .role(Role.STUDENT)
                    .build();

            RegisterRequestDTO student3DTO = RegisterRequestDTO.builder()
                    .username("charlie")
                    .email("charlie@example.com")
                    .password("password123")
                    .fullName("Charlie Brown")
                    .departmentId(d3.getId())
                    .role(Role.STUDENT)
                    .build();

            authService.register(student1DTO);
            authService.register(student2DTO);
            authService.register(student3DTO);
            System.out.println("Seeded 3 students.");

            // --- Courses ---
            Course c1 = new Course();
            c1.setName("Introduction to Java");
            c1.setDescription("Learn Java from scratch");
            c1.setPrice(5000);
            c1.setIsPaid(true);
            c1.setTeacher(t1);
            c1.setDepartment(d1);

            Course c2 = new Course();
            c2.setName("Basic Mathematics");
            c2.setDescription("Fundamental math concepts");
            c2.setPrice(0);
            c2.setIsPaid(false);
            c2.setTeacher(t2);
            c2.setDepartment(d2);

            Course c3 = new Course();
            c3.setName("Physics 101");
            c3.setDescription("Basics of Physics");
            c3.setPrice(2000);
            c3.setIsPaid(true);
            c3.setTeacher(t3);
            c3.setDepartment(d3);

            courseRepository.saveAll(List.of(c1, c2, c3));
            System.out.println("Seeded 3 courses.");
        };
    }
}