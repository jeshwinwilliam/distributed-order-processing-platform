package com.jeshwin.eventmesh.order.api;

import com.jeshwin.eventmesh.common.model.LineItem;

import java.util.List;

public record CreateOrderRequest(
        String customerId,
        String region,
        String salesChannel,
        List<LineItem> items
) {
}

