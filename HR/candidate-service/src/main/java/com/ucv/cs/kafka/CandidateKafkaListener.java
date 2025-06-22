package com.ucv.cs.kafka;

import com.ucv.cs.dto.CandidateCreatedEvent;
import com.ucv.cs.entity.Candidate;
import com.ucv.cs.repository.CandidateRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CandidateKafkaListener {

    private final CandidateRepository candidateRepository;

    public CandidateKafkaListener(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }

    @KafkaListener(topics = "candidates-topic", groupId = "candidate-service")
    public void listen(CandidateCreatedEvent event) {
        Candidate candidate = new Candidate();
        candidate.setUserId(Long.parseLong(event.getUserId()));
        candidate.setName(event.getUsername());
        candidate.setPosition("Unknown");  // Placeholder
        candidate.setCvData(null);         // CV not available yet

        candidateRepository.save(candidate);
        System.out.println("✅ Candidate created from Kafka event: " + event.getUsername());
    }
}
