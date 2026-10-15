package com.igor.inventory.service;


import com.igor.kafka_dto_contracts.order.event.OrderPlacedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {
    private static final Logger logger = LoggerFactory.getLogger(InventoryService.class);

    private final InventoryProcessor inventoryProcessor;

    public InventoryService(InventoryProcessor inventoryProcessor) {
        this.inventoryProcessor = inventoryProcessor;
    }

    @KafkaListener(topics = "order-placed")
    public void reserveInventory(OrderPlacedEvent orderPlacedEvent) {
        logger.info("Received Order Placed Event {}", orderPlacedEvent);
        inventoryProcessor.processOrder(orderPlacedEvent);
        logger.info("Order Placed Successfully");
    }
}
