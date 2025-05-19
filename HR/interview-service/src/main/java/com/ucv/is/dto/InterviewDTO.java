package com.ucv.is.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class InterviewDTO {
    private Long id;
    private Long candidateId;
    private List<Long> employeeIds;

    private LocalDateTime scheduledAt;
    private String status;
    private String feedback;
}
