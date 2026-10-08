package com.igor.inventory.service;


import com.igor.inventory.repository.InventoryRepository;
import com.igor.kafka_dto_contracts.inventory.event.InventoryRejectedEvent;
import com.igor.kafka_dto_contracts.inventory.event.InventoryReservedEvent;
import com.igor.kafka_dto_contracts.order.event.OrderPlacedEvent;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public InventoryService(InventoryRepository inventoryRepository, KafkaTemplate<String, Object> kafkaTemplate) {
        this.inventoryRepository = inventoryRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "order-placed")
    public void reserveInventory(OrderPlacedEvent orderPlacedEvent) {
        try {
            inventoryRepository.deductStock(orderPlacedEvent);

            InventoryReservedEvent inventoryReservedEvent = new InventoryReservedEvent(
                    orderPlacedEvent.orderId(),
                    orderPlacedEvent.email()
            );
//            kafkaTemplate.send("inventory-reserved", inventoryReservedEvent);
            // Add key = orderId()
            kafkaTemplate.send("inventory-reserved", inventoryReservedEvent.orderID(), inventoryReservedEvent);

        } catch (Exception e) {
            InventoryRejectedEvent inventoryRejectedEvent = new InventoryRejectedEvent(
                    orderPlacedEvent.orderId(),
                    orderPlacedEvent.email()
            );
//            kafkaTemplate.send("inventory-rejected", inventoryRejectedEvent);
            // Add custom header
            ProducerRecord<String, Object> producerRecord = new ProducerRecord<>("inventory-rejected", inventoryRejectedEvent.orderId(), inventoryRejectedEvent);
            producerRecord.headers().add("someHeader", String.valueOf(new Random().nextLong()).getBytes());
            kafkaTemplate.send(producerRecord);
        }
    }
}
