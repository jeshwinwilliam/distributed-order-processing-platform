package com.jeshwin.eventmesh.order.config;

import com.jeshwin.eventmesh.common.config.KafkaTopics;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class OrderTopicConfig {

    @Bean
    NewTopic orderCreatedTopic() {
        return TopicBuilder.name(KafkaTopics.ORDER_CREATED).partitions(3).replicas(1).build();
    }

    @Bean
    NewTopic inventoryStatusTopic() {
        return TopicBuilder.name(KafkaTopics.INVENTORY_STATUS).partitions(3).replicas(1).build();
    }

    @Bean
    NewTopic fraudStatusTopic() {
        return TopicBuilder.name(KafkaTopics.FRAUD_STATUS).partitions(3).replicas(1).build();
    }

    @Bean
    NewTopic orderStatusTopic() {
        return TopicBuilder.name(KafkaTopics.ORDER_STATUS).partitions(3).replicas(1).build();
    }
}

