package com.jeshwin.eventmesh.common.event;

import java.time.Instant;

public record InventoryStatusEvent(
        String orderId,
        boolean reserved,
        String warehouseCode,
        String details,
        Instant processedAt
) {
}

