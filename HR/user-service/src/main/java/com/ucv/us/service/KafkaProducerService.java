package com.ucv.us.service;

import com.ucv.us.dto.CandidateCreatedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, CandidateCreatedEvent> kafkaTemplate;

    @Value("${users.topic}")
    private String topicName;

    public KafkaProducerService(KafkaTemplate<String, CandidateCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendCandidateCreatedEvent(CandidateCreatedEvent event) {
        kafkaTemplate.send(topicName, event);
    }
}

