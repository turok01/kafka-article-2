package com.igor.inventory.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "processed_order_id")
@NoArgsConstructor
@Getter
@Setter
public class ProcessedOrderId {
    @Id
    @Column(name = "order_id", length = 36)
    private String orderId;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    public ProcessedOrderId(String orderId) {
        this.orderId = orderId;
        this.processedAt = LocalDateTime.now();
    }
}
