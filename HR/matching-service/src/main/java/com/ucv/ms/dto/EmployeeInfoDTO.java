package com.ucv.ms.dto;

import lombok.Data;
import java.util.List;

@Data
public class EmployeeInfoDTO {
    private Long id;
    private String name;
    private String jobTitle;
    private List<String> skills;
    private String department;
}
