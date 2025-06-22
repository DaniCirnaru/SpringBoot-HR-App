package com.ucv.cs.mapper;

import com.ucv.cs.dto.CandidateDTO;
import com.ucv.cs.dto.CandidateInfoDTO;
import com.ucv.cs.dto.CreateCandidateDTO;
import com.ucv.cs.entity.Candidate;
import org.springframework.stereotype.Component;

@Component
public class CandidateMapper {
    public CandidateDTO toDto(Candidate c) {
        CandidateDTO d = new CandidateDTO();
        d.setId(c.getId());
        d.setName(c.getName());
        d.setUserId(c.getUserId());
        d.setPosition(c.getPosition());
        d.setCvData(c.getCvData());
        d.setRecognizedSkills(c.getRecognizedSkills());
        d.setCvParseStatus(c.getCvParseStatus());
        return d;
    }

    public Candidate toEntity(CreateCandidateDTO dto) {
        Candidate c = new Candidate();
        c.setName(dto.getName());
        c.setUserId(dto.getUserId());
        c.setPosition(dto.getPosition());
        c.setCvData(dto.getCvData());
        return c;
    }
    public CandidateInfoDTO toCandidateInfoDTO(Candidate candidate) {
        CandidateInfoDTO dto = new CandidateInfoDTO();
        dto.setId(candidate.getId());
        dto.setName(candidate.getName());
        dto.setPosition(candidate.getPosition());
        dto.setRecognizedSkills(candidate.getRecognizedSkills()); // if stored or derived
        return dto;
    }
}