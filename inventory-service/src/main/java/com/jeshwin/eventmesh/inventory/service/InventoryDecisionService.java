package com.jeshwin.eventmesh.inventory.service;

import com.jeshwin.eventmesh.common.config.KafkaTopics;
import com.jeshwin.eventmesh.common.event.InventoryStatusEvent;
import com.jeshwin.eventmesh.common.event.OrderCreatedEvent;
import com.jeshwin.eventmesh.inventory.repository.InventoryRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class InventoryDecisionService {

    private final InventoryRepository inventoryRepository;
    private final KafkaTemplate<String, InventoryStatusEvent> kafkaTemplate;

    public InventoryDecisionService(InventoryRepository inventoryRepository,
                                    KafkaTemplate<String, InventoryStatusEvent> kafkaTemplate) {
        this.inventoryRepository = inventoryRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = KafkaTopics.ORDER_CREATED, groupId = "inventory-service")
    public void reserveInventory(OrderCreatedEvent event) {
        boolean reserved = event.items().stream()
                .allMatch(item -> inventoryRepository.reserve(item.sku(), item.quantity()));

        InventoryStatusEvent statusEvent = new InventoryStatusEvent(
                event.orderId(),
                reserved,
                event.region() + "-FC",
                reserved ? "Inventory locked for wave picking." : "Insufficient stock in the preferred fulfillment zone.",
                Instant.now()
        );

        kafkaTemplate.send(KafkaTopics.INVENTORY_STATUS, event.orderId(), statusEvent);
    }
}

