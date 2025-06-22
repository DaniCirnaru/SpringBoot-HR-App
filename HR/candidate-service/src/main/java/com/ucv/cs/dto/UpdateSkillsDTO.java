package com.ucv.cs.dto;

import lombok.Data;
import java.util.List;

@Data
public class UpdateSkillsDTO {
    private List<String> recognizedSkills;
}
