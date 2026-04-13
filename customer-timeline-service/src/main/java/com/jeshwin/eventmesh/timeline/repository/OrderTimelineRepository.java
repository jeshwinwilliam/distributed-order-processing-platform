package com.jeshwin.eventmesh.timeline.repository;

import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class OrderTimelineRepository {

    private final Map<String, List<TimelineEntry>> timelines = new ConcurrentHashMap<>();

    public void append(String orderId, String stage, String details) {
        timelines.computeIfAbsent(orderId, key -> new ArrayList<>())
                .add(new TimelineEntry(stage, details, Instant.now()));
    }

    public List<TimelineEntry> findByOrderId(String orderId) {
        return timelines.getOrDefault(orderId, List.of());
    }

    public record TimelineEntry(String stage, String details, Instant timestamp) {
    }
}

