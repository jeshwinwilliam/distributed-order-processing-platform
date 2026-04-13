package com.jeshwin.eventmesh.orchestration.repository;

import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class SagaStateRepository {

    private final Map<String, SagaState> states = new ConcurrentHashMap<>();

    public SagaState getOrCreate(String orderId) {
        return states.computeIfAbsent(orderId, id -> new SagaState());
    }

    public static class SagaState {
        private Boolean inventoryReserved;
        private Boolean fraudApproved;
        private boolean completed;
        private Instant updatedAt = Instant.now();

        public Boolean getInventoryReserved() {
            return inventoryReserved;
        }

        public void setInventoryReserved(Boolean inventoryReserved) {
            this.inventoryReserved = inventoryReserved;
            this.updatedAt = Instant.now();
        }

        public Boolean getFraudApproved() {
            return fraudApproved;
        }

        public void setFraudApproved(Boolean fraudApproved) {
            this.fraudApproved = fraudApproved;
            this.updatedAt = Instant.now();
        }

        public boolean isCompleted() {
            return completed;
        }

        public void markCompleted() {
            this.completed = true;
            this.updatedAt = Instant.now();
        }
    }
}

