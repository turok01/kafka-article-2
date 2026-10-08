package com.igor.analytics.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaConsumerConfig {

    @Bean
    public DefaultErrorHandler kafkaErrorHandler() {
        return new DefaultErrorHandler(
                (record, exception) -> {
                    // Returning normally marks this record as recovered/skipped.
                    System.err.printf(
                            "Skipping record: topic=%s partition=%d offset=%d cause=%s%n",
                            record.topic(),
                            record.partition(),
                            record.offset(),
                            exception.getMessage()
                    );
                },
                new FixedBackOff(1000L, 2L)
        );
    }
}