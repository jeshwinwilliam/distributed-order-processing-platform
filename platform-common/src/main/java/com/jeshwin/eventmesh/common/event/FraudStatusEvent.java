package com.jeshwin.eventmesh.common.event;

import java.time.Instant;

public record FraudStatusEvent(
        String orderId,
        boolean approved,
        int riskScore,
        String details,
        Instant processedAt
) {
}

