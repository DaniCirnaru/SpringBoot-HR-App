package com.ucv.es.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeInfoDTO {
    private Long id;
    private String name;
    private String jobTitle;
    private String department;
    private List<String> skills;
}
