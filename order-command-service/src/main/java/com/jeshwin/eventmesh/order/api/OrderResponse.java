package com.jeshwin.eventmesh.order.api;

import java.time.Instant;

public record OrderResponse(
        String orderId,
        String status,
        Instant createdAt
) {
}

