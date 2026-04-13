package com.jeshwin.eventmesh.inventory.repository;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InventoryRepository {

    private final Map<String, Integer> stockBySku = new ConcurrentHashMap<>();

    public InventoryRepository() {
        stockBySku.put("PULSE-X1", 30);
        stockBySku.put("VANTA-PACK", 50);
        stockBySku.put("SOLAR-SLEEVE", 12);
    }

    public synchronized boolean reserve(String sku, int quantity) {
        int current = stockBySku.getOrDefault(sku, 0);
        if (current < quantity) {
            return false;
        }
        stockBySku.put(sku, current - quantity);
        return true;
    }
}

