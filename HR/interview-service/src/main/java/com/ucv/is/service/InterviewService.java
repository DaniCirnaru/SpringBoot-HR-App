package com.ucv.is.service;

import com.ucv.is.client.CandidateClient;
import com.ucv.is.client.EmployeeClient;
import com.ucv.is.dto.CandidateInfoDTO;
import com.ucv.is.dto.EmployeeInfoDTO;
import com.ucv.is.dto.InterviewDTO;
import com.ucv.is.entity.Interview;
import com.ucv.is.mapper.InterviewMapper;
import com.ucv.is.repository.InterviewRepository;
import feign.FeignException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InterviewService {

    private final InterviewRepository repository;
    private final CandidateClient candidateClient;
    private final EmployeeClient employeeClient;

    public InterviewService(InterviewRepository repository,
                            CandidateClient candidateClient,
                            EmployeeClient employeeClient) {
        this.repository = repository;
        this.candidateClient = candidateClient;
        this.employeeClient = employeeClient;
    }

    public InterviewDTO scheduleInterview(InterviewDTO dto) {
        validateInterviewData(dto);

        // ❗ Validate candidate exists
        ensureCandidateExists(dto.getCandidateId());

        // ❗ Validate all employees exist
        dto.getEmployeeIds().forEach(this::ensureEmployeeExists);

        // 🧱 Default values
        Interview interview = InterviewMapper.toEntity(dto);
        interview.setStatus("SCHEDULED");
        interview.setScheduledAt(Optional.ofNullable(dto.getScheduledAt())
                .orElse(LocalDateTime.now().plusDays(1)));

        Interview saved = repository.save(interview);
        return InterviewMapper.toDTO(saved);
    }

    public List<InterviewDTO> getByCandidate(Long candidateId) {
        return repository.findByCandidateId(candidateId)
                .stream()
                .map(InterviewMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<InterviewDTO> getByEmployee(Long employeeId) {
        return repository.findByEmployeeId(employeeId)
                .stream()
                .map(InterviewMapper::toDTO)
                .collect(Collectors.toList());
    }

    // ✅ Internal validation helpers

    private void validateInterviewData(InterviewDTO dto) {
        if (dto.getCandidateId() == null || dto.getEmployeeIds() == null || dto.getEmployeeIds().isEmpty()) {
            throw new IllegalArgumentException("Candidate ID and at least one Employee ID must be provided.");
        }

        if (dto.getScheduledAt() != null && dto.getScheduledAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Scheduled date must be in the future.");
        }
    }

    private void ensureCandidateExists(Long candidateId) {
        try {
            CandidateInfoDTO candidate = candidateClient.getCandidateInfo(candidateId);
        } catch (FeignException.NotFound e) {
            throw new IllegalArgumentException("❌ Candidate not found with ID: " + candidateId);
        }
    }

    private void ensureEmployeeExists(Long employeeId) {
        try {
            EmployeeInfoDTO emp = employeeClient.getEmployeeInfo(employeeId);
        } catch (FeignException.NotFound e) {
            throw new IllegalArgumentException("❌ Employee not found with ID: " + employeeId);
        }
    }
}
