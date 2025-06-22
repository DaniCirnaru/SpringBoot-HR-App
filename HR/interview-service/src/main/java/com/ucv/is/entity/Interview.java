package com.ucv.is.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "interviews")
@Data
public class Interview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long candidateId;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "interview_employees", joinColumns = @JoinColumn(name = "interview_id"))
    @Column(name = "employee_id")
    private List<Long> employeeIds;

    private LocalDateTime scheduledAt;

    private String status; // SCHEDULED, COMPLETED, CANCELLED
    private String feedback;
}
