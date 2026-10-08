package com.igor.kafka_dto_contracts.order.event;

public record OrderPlacedEvent(String orderId, String email, String productName, Integer quantity) {
}
