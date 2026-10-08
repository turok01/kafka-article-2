package com.igor.analytics.service;


import com.igor.kafka_dto_contracts.inventory.event.InventoryRejectedEvent;
import com.igor.kafka_dto_contracts.inventory.event.InventoryReservedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsService {
    private static final Logger logger = LoggerFactory.getLogger(AnalyticsService.class);

    @KafkaListener(topics = "inventory-reserved")
    public void sendNotificationIfReserved(InventoryReservedEvent inventoryReservedEvent) {
        logger.info("Received inventory reserved event: {}", inventoryReservedEvent);
    }

    @KafkaListener(topics = "inventory-rejected")
    public void sendNotificationIfRejected(InventoryRejectedEvent inventoryRejectedEvent) {
        logger.info("Received inventory rejected event: {}", inventoryRejectedEvent);
    }
}
