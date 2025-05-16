package com.ucv.us.service;

import com.ucv.us.dto.CandidateCreatedEvent;
import com.ucv.us.dto.EmployeeCreatedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${users.topics.candidate}")
    private String candidateTopic;

    @Value("${users.topics.employee}")
    private String employeeTopic;

    public KafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendCandidateCreatedEvent(CandidateCreatedEvent event) {
        kafkaTemplate.send(candidateTopic, event);
    }

    public void sendEmployeeCreatedEvent(EmployeeCreatedEvent event) {
        kafkaTemplate.send(employeeTopic, event);
    }
}
