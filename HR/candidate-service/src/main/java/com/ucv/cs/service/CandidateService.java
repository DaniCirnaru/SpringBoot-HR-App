package com.ucv.cs.service;

import com.ucv.cs.dto.CandidateDTO;
import com.ucv.cs.dto.CreateCandidateDTO;
import com.ucv.cs.entity.Candidate;
import com.ucv.cs.entity.CvParseStatus;
import com.ucv.cs.exception.ResourceNotFoundException;
import com.ucv.cs.mapper.CandidateMapper;
import com.ucv.cs.repository.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private CandidateMapper candidateMapper;

    public CandidateDTO createCandidate(CreateCandidateDTO dto) {
        Candidate c = candidateMapper.toEntity(dto);
        Candidate s = candidateRepository.save(c);
        return candidateMapper.toDto(s);
    }

    public List<CandidateDTO> getAllCandidates() {
        return candidateRepository.findAll().stream()
                .map(candidateMapper::toDto)
                .collect(Collectors.toList());
    }

    public CandidateDTO getCandidateById(Long id) {
        Candidate c = candidateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate"));
        return candidateMapper.toDto(c);
    }

    public CandidateDTO updateCandidate(Long id, CreateCandidateDTO dto) {
        Candidate c = candidateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate"));
        c.setName(dto.getName());
        c.setUserId(dto.getUserId());
        c.setPosition(dto.getPosition());
        c.setCvData(dto.getCvData());
        Candidate u = candidateRepository.save(c);
        return candidateMapper.toDto(u);
    }

    public void deleteCandidate(Long id) {
        if (!candidateRepository.existsById(id)) {
            throw new ResourceNotFoundException("Candidate");
        }
        candidateRepository.deleteById(id);
    }

    // ✅ New method for uploading CVs
    public void uploadCv(Long candidateId, MultipartFile file) {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate"));

        try {
            candidate.setCvData(file.getBytes());
            candidate.setCvParseStatus(CvParseStatus.PENDING);
            candidateRepository.save(candidate);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload CV", e);
        }
    }
}
