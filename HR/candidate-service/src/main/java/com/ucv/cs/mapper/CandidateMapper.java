package com.ucv.cs.mapper;

import com.ucv.cs.dto.CandidateDTO;
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
}