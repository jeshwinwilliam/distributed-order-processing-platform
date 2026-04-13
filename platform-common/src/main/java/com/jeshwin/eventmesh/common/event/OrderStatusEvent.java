package com.jeshwin.eventmesh.common.event;

import java.time.Instant;

public record OrderStatusEvent(
        String orderId,
        String status,
        String details,
        Instant processedAt
) {
}

