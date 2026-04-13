package com.jeshwin.eventmesh.fraud.service;

import com.jeshwin.eventmesh.common.config.KafkaTopics;
import com.jeshwin.eventmesh.common.event.FraudStatusEvent;
import com.jeshwin.eventmesh.common.event.OrderCreatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class FraudDecisionService {

    private final KafkaTemplate<String, FraudStatusEvent> kafkaTemplate;

    public FraudDecisionService(KafkaTemplate<String, FraudStatusEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = KafkaTopics.ORDER_CREATED, groupId = "fraud-service")
    public void review(OrderCreatedEvent event) {
        int riskScore = calculateRisk(event);
        boolean approved = riskScore < 70;

        FraudStatusEvent statusEvent = new FraudStatusEvent(
                event.orderId(),
                approved,
                riskScore,
                approved ? "Adaptive rules cleared the order." : "Risk model requested manual intervention.",
                Instant.now()
        );
        kafkaTemplate.send(KafkaTopics.FRAUD_STATUS, event.orderId(), statusEvent);
    }

    private int calculateRisk(OrderCreatedEvent event) {
        int score = (int) Math.min(50, event.orderTotal() / 25);
        if ("cross-border".equalsIgnoreCase(event.salesChannel())) {
            score += 20;
        }
        if ("LATAM".equalsIgnoreCase(event.region()) || "APAC".equalsIgnoreCase(event.region())) {
            score += 15;
        }
        return score;
    }
}

