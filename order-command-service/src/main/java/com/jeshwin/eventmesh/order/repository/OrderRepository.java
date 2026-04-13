package com.jeshwin.eventmesh.order.repository;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    private final Map<String, Snapshot> store = new ConcurrentHashMap<>();

    public void save(String orderId, String customerId, double total) {
        store.put(orderId, new Snapshot(orderId, customerId, total, "PENDING", Instant.now()));
    }

    public record Snapshot(String orderId, String customerId, double total, String status, Instant createdAt) {
    }
}

