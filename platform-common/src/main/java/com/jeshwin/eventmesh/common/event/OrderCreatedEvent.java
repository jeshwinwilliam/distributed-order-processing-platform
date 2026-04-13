package com.jeshwin.eventmesh.common.event;

import com.jeshwin.eventmesh.common.model.LineItem;

import java.time.Instant;
import java.util.List;

public record OrderCreatedEvent(
        String orderId,
        String customerId,
        String region,
        String salesChannel,
        List<LineItem> items,
        double orderTotal,
        Instant createdAt
) {
}

