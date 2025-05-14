package com.ucv.cs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidateDTO {
    private Long id;
    private String name;
    private Long userId;
    private String position;
    private byte[] cvData;
}
