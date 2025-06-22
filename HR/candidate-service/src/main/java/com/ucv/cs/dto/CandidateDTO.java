package com.ucv.cs.dto;

import com.ucv.cs.entity.CvParseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidateDTO {
    private Long id;
    private String name;
    private Long userId;
    private String position;
    private byte[] cvData;
    private List<String> recognizedSkills;
    private CvParseStatus cvParseStatus;
}
