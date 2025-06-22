package com.ucv.cs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidateInfoDTO {
    private Long id;
    private String name;
    private String position;
    private List<String> recognizedSkills;
}
