package com.assignment.jackpot.service;

import com.assignment.jackpot.dto.BetEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaBetProducer {
    private final KafkaTemplate<String, BetEvent> kafkaTemplate;

    public void sendBetEvent(String topic,BetEvent betEvent) {
            kafkaTemplate.send(topic, betEvent);
            log.info("Sent bet event to Kafka: {}", betEvent);
    }
}
