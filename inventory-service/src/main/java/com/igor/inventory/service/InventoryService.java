package com.igor.inventory.service;


import com.igor.inventory.model.OutboxEvent;
import com.igor.inventory.model.ProcessedOrderId;
import com.igor.inventory.repository.InventoryRepository;
import com.igor.inventory.repository.OutboxEventRepository;
import com.igor.inventory.repository.ProcessedOrderIdRepository;
import com.igor.kafka_dto_contracts.inventory.event.InventoryRejectedEvent;
import com.igor.kafka_dto_contracts.inventory.event.InventoryReservedEvent;
import com.igor.kafka_dto_contracts.order.event.OrderPlacedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

@Service
public class InventoryService {
    private static Logger logger = LoggerFactory.getLogger(InventoryService.class);

    private final InventoryProcessor inventoryProcessor;

    public InventoryService(InventoryProcessor inventoryProcessor) {
        this.inventoryProcessor = inventoryProcessor;
    }

    @KafkaListener(topics = "order-placed")
    public void reserveInventory(OrderPlacedEvent orderPlacedEvent) {
        inventoryProcessor.processOrder(orderPlacedEvent);
    }
}
