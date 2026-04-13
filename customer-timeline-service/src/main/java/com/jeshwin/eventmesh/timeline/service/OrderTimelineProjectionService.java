package com.jeshwin.eventmesh.timeline.service;

import com.jeshwin.eventmesh.common.config.KafkaTopics;
import com.jeshwin.eventmesh.common.event.FraudStatusEvent;
import com.jeshwin.eventmesh.common.event.InventoryStatusEvent;
import com.jeshwin.eventmesh.common.event.OrderCreatedEvent;
import com.jeshwin.eventmesh.common.event.OrderStatusEvent;
import com.jeshwin.eventmesh.timeline.repository.OrderTimelineRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderTimelineProjectionService {

    private final OrderTimelineRepository repository;

    public OrderTimelineProjectionService(OrderTimelineRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(topics = KafkaTopics.ORDER_CREATED, groupId = "customer-timeline-service")
    public void onOrderCreated(OrderCreatedEvent event) {
        repository.append(event.orderId(), "ORDER_CREATED",
                "Received order from " + event.salesChannel() + " channel for region " + event.region() + ".");
    }

    @KafkaListener(topics = KafkaTopics.INVENTORY_STATUS, groupId = "customer-timeline-service")
    public void onInventory(InventoryStatusEvent event) {
        repository.append(event.orderId(), "INVENTORY_GATE", event.details());
    }

    @KafkaListener(topics = KafkaTopics.FRAUD_STATUS, groupId = "customer-timeline-service")
    public void onFraud(FraudStatusEvent event) {
        repository.append(event.orderId(), "FRAUD_GATE", event.details() + " Score=" + event.riskScore());
    }

    @KafkaListener(topics = KafkaTopics.ORDER_STATUS, groupId = "customer-timeline-service")
    public void onOrderStatus(OrderStatusEvent event) {
        repository.append(event.orderId(), event.status(), event.details());
    }
}

