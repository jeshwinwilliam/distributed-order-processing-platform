package com.jeshwin.eventmesh.timeline.api;

import com.jeshwin.eventmesh.timeline.repository.OrderTimelineRepository;
import com.jeshwin.eventmesh.timeline.repository.OrderTimelineRepository.TimelineEntry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/timelines/orders")
public class OrderTimelineController {

    private final OrderTimelineRepository repository;

    public OrderTimelineController(OrderTimelineRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/{orderId}")
    public List<TimelineEntry> getTimeline(@PathVariable String orderId) {
        return repository.findByOrderId(orderId);
    }
}

