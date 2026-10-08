package com.igor.kafka_dto_contracts.inventory.event;

public record InventoryRejectedEvent(String orderId, String email) {
}
