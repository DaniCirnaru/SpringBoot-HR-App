package com.ucv.cs.service;

import com.ucv.cs.dto.CandidateDTO;
import com.ucv.cs.dto.CandidateInfoDTO;
import com.ucv.cs.dto.CreateCandidateDTO;
import com.ucv.cs.entity.Candidate;
import com.ucv.cs.entity.CvParseStatus;
import com.ucv.cs.exception.ResourceNotFoundException;
import com.ucv.cs.mapper.CandidateMapper;
import com.ucv.cs.repository.CandidateRepository;
import com.ucv.cs.util.SkillExtractor;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
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

    public CandidateInfoDTO getCandidateInfoById(Long id) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate"));

        return new CandidateInfoDTO(
                candidate.getId(),
                candidate.getName(),
                candidate.getPosition()
        );
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
            byte[] cvBytes = file.getBytes();
            candidate.setCvData(cvBytes);

            // Use Apache Tika to extract text
            Tika tika = new Tika();
            String text = tika.parseToString(new ByteArrayInputStream(cvBytes));

            // Use SkillExtractor to extract skills
            List<String> skills = SkillExtractor.extractSkills(text);
            candidate.setRecognizedSkills(skills);
            candidate.setCvParseStatus(CvParseStatus.PARSED);

        } catch (Exception e) {
            candidate.setCvParseStatus(CvParseStatus.FAILED);
            throw new RuntimeException("Failed to parse CV and extract skills", e);
        }

        candidateRepository.save(candidate);
    }
}
