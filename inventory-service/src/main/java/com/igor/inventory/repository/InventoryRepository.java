package com.igor.inventory.repository;


import com.igor.kafka_dto_contracts.order.event.OrderPlacedEvent;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InventoryRepository {
    private final Map<String, Integer> inventory = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        inventory.put("Iphone 18 Pro", 5);
        inventory.put("Iphone 18 Pro Max Burgundy", 10);
        inventory.put("Mac Pro", 6);
    }

    public void deductStock(OrderPlacedEvent orderPlacedEvent) {
        Integer result = inventory.computeIfPresent(orderPlacedEvent.productName(),
                (name, quantity) -> {
            if (quantity < orderPlacedEvent.quantity()) {
                throw new RuntimeException("Quantity exceeded");
            }
            return quantity - orderPlacedEvent.quantity();
                });
        if (result == null) {
            throw new RuntimeException("Stock exceeded");
        }
    }
}
