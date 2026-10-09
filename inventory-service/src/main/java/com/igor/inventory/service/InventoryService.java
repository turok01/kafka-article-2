package com.igor.inventory.service;


import com.igor.inventory.repository.InventoryRepository;
import com.igor.kafka_dto_contracts.inventory.event.InventoryRejectedEvent;
import com.igor.kafka_dto_contracts.inventory.event.InventoryReservedEvent;
import com.igor.kafka_dto_contracts.order.event.OrderPlacedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static Logger logger = LoggerFactory.getLogger(InventoryService.class);
    HashMap

    public InventoryService(InventoryRepository inventoryRepository, KafkaTemplate<String, Object> kafkaTemplate) {
        this.inventoryRepository = inventoryRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional
    @KafkaListener(topics = "order-placed")
    public void reserveInventory(OrderPlacedEvent orderPlacedEvent) {
        int count = inventoryRepository.deductStock(orderPlacedEvent.productName(), orderPlacedEvent.quantity());

        if (count > 0) {
            InventoryReservedEvent inventoryReservedEvent = new InventoryReservedEvent(
                    orderPlacedEvent.orderId(),
                    orderPlacedEvent.email()
            );

            kafkaTemplate.send("inventory-reserved", inventoryReservedEvent.orderID(), inventoryReservedEvent);
            logger.info("Sending inventory reserved event in thread {}", Thread.currentThread().getName());
        }
        else {
            InventoryRejectedEvent inventoryRejectedEvent = new InventoryRejectedEvent(
                    orderPlacedEvent.orderId(),
                    orderPlacedEvent.email()
            );
            CompletableFuture<SendResult<String, Object>> future =
                    kafkaTemplate.send("inventory-rejected", inventoryRejectedEvent.orderId(), inventoryRejectedEvent);

            future.whenComplete((result, throwable) -> {
                if (throwable != null) {
                    logger.info("Error sending inventory rejected event in thread {}", Thread.currentThread().getName());
                }
                else {
                    logger.info("Success sending inventory rejected event in thread {}", Thread.currentThread().getName());
                }
            });
        }
    }
}
