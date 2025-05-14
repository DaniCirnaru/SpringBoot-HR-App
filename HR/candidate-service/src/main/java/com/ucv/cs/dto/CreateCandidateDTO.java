package com.ucv.cs.dto;

import lombok.Data;

@Data
public class CreateCandidateDTO {
    private String name;
    private Long userId;

    private String position;

    private byte[] cvData;
}
