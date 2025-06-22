package com.ucv.ms.dto;

import lombok.Data;

@Data
public class MatchResultDTO {
    private Long employeeId;
    private String name;
    private String jobTitle;
    private double score;
}
