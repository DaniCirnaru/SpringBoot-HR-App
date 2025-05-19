package com.ucv.is.mapper;

import com.ucv.is.dto.InterviewDTO;
import com.ucv.is.entity.Interview;

public class InterviewMapper {

    public static InterviewDTO toDTO(Interview interview) {
        InterviewDTO dto = new InterviewDTO();
        dto.setId(interview.getId());
        dto.setCandidateId(interview.getCandidateId());
        dto.setEmployeeIds(interview.getEmployeeIds());
        dto.setScheduledAt(interview.getScheduledAt());
        dto.setStatus(interview.getStatus());
        dto.setFeedback(interview.getFeedback());
        return dto;
    }

    public static Interview toEntity(InterviewDTO dto) {
        Interview interview = new Interview();
        interview.setId(dto.getId());
        interview.setCandidateId(dto.getCandidateId());
        interview.setEmployeeIds(dto.getEmployeeIds());
        interview.setScheduledAt(dto.getScheduledAt());
        interview.setStatus(dto.getStatus());
        interview.setFeedback(dto.getFeedback());
        return interview;
    }
}
