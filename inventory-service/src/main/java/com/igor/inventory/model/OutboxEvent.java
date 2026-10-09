package com.igor.inventory.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "outbox_event")
@NoArgsConstructor
@Getter
@Setter
public class OutboxEvent {
    @Id
    @Column(name = "key", length = 36)
    private String key;
    @Column(name = "topic", nullable = false)
    private String topic;
    @Column(name = "payload", nullable = false)
    private String payload;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "sent")
    private Boolean sent;

    public OutboxEvent(String key, String topic, String payload) {
        this.key = key;
        this.topic = topic;
        this.payload = payload;
        this.createdAt = LocalDateTime.now();
        this.sent = false;
    }

    // empty constructor, getters, setters this.sent = sent;
}
