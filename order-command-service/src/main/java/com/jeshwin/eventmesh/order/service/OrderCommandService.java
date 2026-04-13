package com.jeshwin.eventmesh.order.service;

import com.jeshwin.eventmesh.common.config.KafkaTopics;
import com.jeshwin.eventmesh.common.event.OrderCreatedEvent;
import com.jeshwin.eventmesh.common.model.LineItem;
import com.jeshwin.eventmesh.order.api.CreateOrderRequest;
import com.jeshwin.eventmesh.order.api.OrderResponse;
import com.jeshwin.eventmesh.order.repository.OrderRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class OrderCommandService {

    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;
    private final OrderRepository orderRepository;

    public OrderCommandService(KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate, OrderRepository orderRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.orderRepository = orderRepository;
    }

    public OrderResponse createOrder(CreateOrderRequest request) {
        String orderId = UUID.randomUUID().toString();
        double total = calculateTotal(request.items());
        Instant createdAt = Instant.now();

        orderRepository.save(orderId, request.customerId(), total);

        OrderCreatedEvent event = new OrderCreatedEvent(
                orderId,
                request.customerId(),
                request.region(),
                request.salesChannel(),
                normalizeItems(request.items()),
                total,
                createdAt
        );
        kafkaTemplate.send(KafkaTopics.ORDER_CREATED, orderId, event);

        return new OrderResponse(orderId, "PENDING_SAGA", createdAt);
    }

    private double calculateTotal(List<LineItem> items) {
        return normalizeItems(items).stream()
                .mapToDouble(item -> item.quantity() * item.unitPrice())
                .sum();
    }

    private List<LineItem> normalizeItems(List<LineItem> items) {
        return items == null ? List.of() : items;
    }
}

