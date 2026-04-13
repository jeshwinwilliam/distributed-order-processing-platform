package com.jeshwin.eventmesh.common.config;

public final class KafkaTopics {

    public static final String ORDER_CREATED = "commerce.order.created.v1";
    public static final String INVENTORY_STATUS = "commerce.inventory.status.v1";
    public static final String FRAUD_STATUS = "commerce.fraud.status.v1";
    public static final String ORDER_STATUS = "commerce.order.status.v1";

    private KafkaTopics() {
    }
}

