package com.igor.inventory.scheduler;

import com.igor.inventory.model.OutboxEvent;
import com.igor.inventory.repository.OutboxEventRepository;
import com.igor.kafka_dto_contracts.inventory.event.InventoryRejectedEvent;
import com.igor.kafka_dto_contracts.inventory.event.InventoryReservedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Component
public class InventoryRelayerScheduler {
    private final Logger logger = LoggerFactory.getLogger(InventoryRelayerScheduler.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final OutboxEventRepository outboxEventRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public InventoryRelayerScheduler(OutboxEventRepository outboxEventRepository,
                                     KafkaTemplate<String, Object> kafkaTemplate) {
        this.outboxEventRepository = outboxEventRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedDelay = 5000)
    public void relayOutboxEvents() {
        List<OutboxEvent> outboxEvents = outboxEventRepository.findBySentFalseOrderByCreatedAtAsc(PageRequest.of(0, 100));

        for (OutboxEvent outboxEvent : outboxEvents) {
            try {
                String topic = outboxEvent.getTopic();
                String payloadJson = outboxEvent.getPayload();
                Object payload = (topic.equals("inventory-reserved"))
                        ? objectMapper.readValue(payloadJson, InventoryReservedEvent.class)
                        : objectMapper.readValue(payloadJson, InventoryRejectedEvent.class);

                kafkaTemplate.send(outboxEvent.getTopic(), outboxEvent.getKey(), payload).get(); // wait answer of asynchronous CompletableFuture
                outboxEvent.setSent(true);
                outboxEventRepository.save(outboxEvent);
                logger.info("Event sent to topic: " + outboxEvent.getTopic());
            }
            catch (Exception e) {
                logger.error("Failed to relay outbox event: {}", outboxEvent.getKey(), e);
            }
        }
    }
}
