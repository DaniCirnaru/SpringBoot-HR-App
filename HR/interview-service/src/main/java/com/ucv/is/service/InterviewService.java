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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InterviewService {

    private final InterviewRepository interviewRepository;
    private final CandidateClient candidateClient;
    private final EmployeeClient employeeClient;
    private final InterviewMapper interviewMapper;

    @Autowired
    public InterviewService(InterviewRepository repository,
                            CandidateClient candidateClient,
                            EmployeeClient employeeClient,
                            InterviewMapper interviewMapper) {

        this.interviewRepository = repository;
        this.candidateClient = candidateClient;
        this.employeeClient = employeeClient;
        this.interviewMapper = interviewMapper;
    }

    public List<InterviewDTO> getAllInterviews() {
        return interviewRepository.findAll()
                .stream()
                .map(interviewMapper::toDto)
                .collect(Collectors.toList());
    }


    public InterviewDTO scheduleInterview(InterviewDTO dto) {
        validateInterviewData(dto);

        // ❗ Validate candidate exists
        ensureCandidateExists(dto.getCandidateId());

        // ❗ Validate all employees exist
        dto.getEmployeeIds().forEach(this::ensureEmployeeExists);

        // 🧱 Default values
        Interview interview = interviewMapper.toEntity(dto);
        interview.setStatus("SCHEDULED");
        interview.setScheduledAt(Optional.ofNullable(dto.getScheduledAt())
                .orElse(LocalDateTime.now().plusDays(1)));

        Interview saved = interviewRepository.save(interview);
        return interviewMapper.toDto(saved);

    }

    public List<InterviewDTO> getByCandidate(Long candidateId) {
        return interviewRepository.findByCandidateId(candidateId)
                .stream()
                .map(interviewMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<InterviewDTO> getByEmployee(Long employeeId) {
        return interviewRepository.findByEmployeeId(employeeId)
                .stream()
                .map(interviewMapper::toDto)
                .collect(Collectors.toList());
    }

    // ✅ Internal validation helpers
    public List<InterviewDTO> getByCandidateEmail(String email) {
        CandidateInfoDTO candidateInfo = candidateClient.getCandidateInfoByEmail(email);
        Long candidateId = candidateInfo.getId();

        return interviewRepository.findByCandidateId(candidateId)
                .stream()
                .map(this::mapWithEmployeeNames)
                .toList();
    }

    private InterviewDTO mapWithEmployeeNames(Interview interview) {
        InterviewDTO dto = interviewMapper.toDto(interview);

        List<String> employeeNames = interview.getEmployeeIds().stream()
                .map(id -> {
                    try {
                        return employeeClient.getEmployeeInfo(id).getName(); // Assumes getName() exists
                    } catch (Exception e) {
                        return "Unknown (ID " + id + ")";
                    }
                })
                .toList();

        dto.setEmployeeNames(employeeNames);
        return dto;
    }


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
    public void updateFeedback(Long interviewId, String feedback) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new RuntimeException("Interview not found"));
        interview.setFeedback(feedback);
       interviewRepository.save(interview);
    }
    public List<InterviewDTO> getByEmployeeEmail(String email) {
        Long employeeId = employeeClient.getEmployeeInfoByEmail(email).getId();

        return interviewRepository.findByEmployeeId(employeeId)
                .stream()
                .map(this::mapWithCandidateName)
                .collect(Collectors.toList());
    }
    private InterviewDTO mapWithCandidateName(Interview interview) {
        InterviewDTO dto = interviewMapper.toDto(interview);

        try {
            CandidateInfoDTO candidateInfo = candidateClient.getCandidateInfo(interview.getCandidateId());
            dto.setCandidateName(candidateInfo.getName()); // ✅ Make sure InterviewDTO has setCandidateName
        } catch (Exception e) {
            dto.setCandidateName("Unknown (ID " + interview.getCandidateId() + ")");
        }

        return dto;
    }


    private void ensureEmployeeExists(Long employeeId) {
        try {
            EmployeeInfoDTO emp = employeeClient.getEmployeeInfo(employeeId);
        } catch (FeignException.NotFound e) {
            throw new IllegalArgumentException("❌ Employee not found with ID: " + employeeId);
        }
    }
}
