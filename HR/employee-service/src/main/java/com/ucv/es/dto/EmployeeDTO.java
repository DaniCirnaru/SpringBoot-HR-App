package com.ucv.es.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {

    private Long userId;
    private String name;
    private String workEmail;
    private String jobTitle;
    private String department;
    private String employmentType; // e.g., FULL_TIME, PART_TIME, CONTRACTOR
    private LocalDate hireDate;
    private BigDecimal salary;
    private String location;
    private List<String> skills;

}