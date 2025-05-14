package com.ucv.us.config;

import com.ucv.us.dto.CandidateCreatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class KafkaTestListener {

    @KafkaListener(topics = "quickstart", groupId = "test-group", properties = {
            "spring.kafka.bootstrap-servers=broker:29092"
    })
    public void listen(@Payload CandidateCreatedEvent event) {
        System.out.println("✅ Received event: " + event);
    }

}
