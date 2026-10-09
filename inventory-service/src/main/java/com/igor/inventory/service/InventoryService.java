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

    private final InventoryRepository inventoryRepository;
    private final ProcessedOrderIdRepository processedOrderIdRepository;
    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public InventoryService(InventoryRepository inventoryRepository, ProcessedOrderIdRepository processedOrderIdRepository,
                            OutboxEventRepository outboxEventRepository) {
        this.inventoryRepository = inventoryRepository;
        this.processedOrderIdRepository = processedOrderIdRepository;
        this.outboxEventRepository = outboxEventRepository;
    }

    @Transactional
    @KafkaListener(topics = "order-placed")
    public void reserveInventory(OrderPlacedEvent orderPlacedEvent) {
        try {
            processedOrderIdRepository.save(new ProcessedOrderId(
                    orderPlacedEvent.orderId()
            ));
        } catch (DataIntegrityViolationException e) {
            logger.info("Order {} already processed", orderPlacedEvent.orderId());
            return;
        }

        int count = inventoryRepository.deductStock(orderPlacedEvent.productName(), orderPlacedEvent.quantity());
        String topic = (count > 0) ? "inventory-reserved" : "inventory-rejected";
        Object event = (count > 0)
                ? new InventoryReservedEvent(orderPlacedEvent.orderId(), orderPlacedEvent.email())
                : new InventoryRejectedEvent(orderPlacedEvent.orderId(), orderPlacedEvent.email());
        String jsonPayload;
        try {
            jsonPayload = objectMapper.writeValueAsString(event);
        } catch (Exception e) {
            throw new RuntimeException("Serialization failed for order: " + orderPlacedEvent.orderId(), e);
        }

        OutboxEvent outboxEvent = new OutboxEvent(orderPlacedEvent.orderId(), topic, jsonPayload);
        outboxEventRepository.save(outboxEvent);
    }
}
