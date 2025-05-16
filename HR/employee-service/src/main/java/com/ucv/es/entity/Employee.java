package com.ucv.es.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Table(name = "employees")
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Reference to the user ID from the user-service.
     */
    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "work_email", nullable = false)
    private String workEmail;

    @Column(name = "job_title", nullable = false)
    private String jobTitle;

    @Column(name = "department")
    private String department;

    @Column(name = "employment_type") // e.g., FULL_TIME, PART_TIME
    private String employmentType;

    @Column(name = "hire_date")
    private LocalDate hireDate;

    @Column(name = "salary")
    private BigDecimal salary;

    @Column(name = "location")
    private String location;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "employee_skills", joinColumns = @JoinColumn(name = "employee_id"))
    @Column(name = "skill")
    private List<String> skills;

}
