package com.ucv.us.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeCreatedEvent {
    private String userId;
    private String email;
    private String username;
    private String jobTitle;
    private String department;
    private String employmentType;
    private String hireDate;
    private String location;
}
