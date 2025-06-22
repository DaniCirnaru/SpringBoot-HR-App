package com.ucv.ms.dto;

import lombok.Data;
import java.util.List;

@Data
public class CandidateInfoDTO {
    private Long id;
    private String name;
    private String position;
    private List<String> recognizedSkills;
}
