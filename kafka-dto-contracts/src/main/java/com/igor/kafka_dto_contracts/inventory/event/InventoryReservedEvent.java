package com.igor.kafka_dto_contracts.inventory.event;

public record InventoryReservedEvent(String orderID, String email) {}
