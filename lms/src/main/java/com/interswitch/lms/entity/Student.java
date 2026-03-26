package com.interswitch.lms.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "students")
@Getter
@Setter
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String fullName;

    @Column(length = 20)
    private String grade;

    @Column(length = 100)
    private String enrollmentNumber;

    @OneToOne
    @JoinColumn(name = "auth_id", nullable = false)
    @JsonBackReference
    private Auth auth;

    // Many students belong to one department
    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    @JsonBackReference
    private Department department;
}