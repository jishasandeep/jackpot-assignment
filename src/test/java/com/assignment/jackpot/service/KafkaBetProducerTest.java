package com.assignment.jackpot.service;

import com.assignment.jackpot.dto.BetEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaBetProducerTest {

    @Mock
    private KafkaTemplate<String, BetEvent> kafkaTemplate;

    @InjectMocks
    private KafkaBetProducer producer;

    @Test
    void shouldSendBetEvent() {
        BetEvent event = new BetEvent("bet1", 3005L, 2L, 100);

        assertDoesNotThrow(() -> producer.sendBetEvent("jackpot-bets", event));

        verify(kafkaTemplate, times(1)).send(anyString(), any(BetEvent.class));
    }
}
