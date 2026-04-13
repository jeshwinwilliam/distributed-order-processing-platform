package com.jeshwin.eventmesh.orchestration.service;

import com.jeshwin.eventmesh.common.config.KafkaTopics;
import com.jeshwin.eventmesh.common.event.FraudStatusEvent;
import com.jeshwin.eventmesh.common.event.InventoryStatusEvent;
import com.jeshwin.eventmesh.common.event.OrderStatusEvent;
import com.jeshwin.eventmesh.orchestration.repository.SagaStateRepository;
import com.jeshwin.eventmesh.orchestration.repository.SagaStateRepository.SagaState;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class OrderSagaOrchestrator {

    private final SagaStateRepository sagaStateRepository;
    private final KafkaTemplate<String, OrderStatusEvent> kafkaTemplate;

    public OrderSagaOrchestrator(SagaStateRepository sagaStateRepository,
                                 KafkaTemplate<String, OrderStatusEvent> kafkaTemplate) {
        this.sagaStateRepository = sagaStateRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = KafkaTopics.INVENTORY_STATUS, groupId = "orchestration-service")
    public void onInventory(InventoryStatusEvent event) {
        SagaState state = sagaStateRepository.getOrCreate(event.orderId());
        state.setInventoryReserved(event.reserved());
        emitIfReady(event.orderId(), state);
    }

    @KafkaListener(topics = KafkaTopics.FRAUD_STATUS, groupId = "orchestration-service")
    public void onFraud(FraudStatusEvent event) {
        SagaState state = sagaStateRepository.getOrCreate(event.orderId());
        state.setFraudApproved(event.approved());
        emitIfReady(event.orderId(), state);
    }

    private void emitIfReady(String orderId, SagaState state) {
        if (state.isCompleted()) {
            return;
        }
        if (state.getInventoryReserved() == null || state.getFraudApproved() == null) {
            return;
        }

        boolean approved = state.getInventoryReserved() && state.getFraudApproved();
        OrderStatusEvent statusEvent = new OrderStatusEvent(
                orderId,
                approved ? "APPROVED_FOR_FULFILLMENT" : "REJECTED",
                approved ? "Saga converged successfully and released the order to packing."
                        : "Saga rejected the order because one or more downstream gates failed.",
                Instant.now()
        );

        kafkaTemplate.send(KafkaTopics.ORDER_STATUS, orderId, statusEvent);
        state.markCompleted();
    }
}

