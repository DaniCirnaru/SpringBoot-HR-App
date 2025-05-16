package com.ucv.es.dto;

import lombok.Data;

import java.util.List;

@Data
public class EmployeeCreatedEvent {
    private String userId;
    private String username;
    private String email;
    private String jobTitle;
    private String department;
    private String employmentType;
    private String hireDate;
    private String location;
    private List<String> skills;

}
