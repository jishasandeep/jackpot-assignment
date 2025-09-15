package com.assignment.jackpot.service;

import com.assignment.jackpot.dto.BetEvent;
import com.assignment.jackpot.model.Bet;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaBetConsumer {
    private final JackpotService jackpotService;

    @KafkaListener(topics = "${kafka.topic}", groupId = "jackpot-group")
    public void consume(BetEvent betEvent) {
        log.info("Received event at Kafka consumer {}",betEvent);
        jackpotService.processBet(Bet.fromEvent(betEvent));

    }
}
