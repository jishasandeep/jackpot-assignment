package com.assignment.jackpot.service;

import com.assignment.jackpot.config.KafkaProperties;
import com.assignment.jackpot.dto.BetEvent;
import com.assignment.jackpot.model.Bet;
import com.assignment.jackpot.repository.BetRepository;
import com.sun.jdi.request.DuplicateRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BetServiceTest {

    private BetRepository betRepository;
    private KafkaBetProducer kafkaBetProducer;
    private JackpotService jackpotService;
    private KafkaProperties kafkaProperties;
    private IdempotencyService idempotencyService;
    private BetService betService;

    private final String IDEMPOTENCY_KEY = "idempotency-key-123";
    private final BetEvent betEvent = new BetEvent("bet1", 1001L, 1L, 100.0);

    @BeforeEach
    void setUp() {
        betRepository = mock(BetRepository.class);
        kafkaBetProducer = mock(KafkaBetProducer.class);
        jackpotService = mock(JackpotService.class);
        kafkaProperties = mock(KafkaProperties.class);
        idempotencyService = mock(IdempotencyService.class);

        betService = new BetService(
                betRepository,
                kafkaBetProducer,
                jackpotService,
                kafkaProperties,
                idempotencyService
        );
    }

    @Test
    void placeBet_shouldThrowException_whenIdempotencyKeyExists() {
        when(idempotencyService.isDuplicateRequest(IDEMPOTENCY_KEY)).thenReturn(true);

        assertThrows(DuplicateRequestException.class,
                () -> betService.placeBet(betEvent, IDEMPOTENCY_KEY));

        verifyNoInteractions(betRepository, kafkaBetProducer, jackpotService);
    }

    @Test
    void placeBet_shouldThrowException_whenBetIdAlreadyExists() {
        when(idempotencyService.isDuplicateRequest(IDEMPOTENCY_KEY)).thenReturn(false);
        when(betRepository.existsById(betEvent.betId())).thenReturn(true);

        assertThrows(DuplicateRequestException.class,
                () -> betService.placeBet(betEvent, IDEMPOTENCY_KEY));

        verify(betRepository).existsById("bet1");
        verifyNoMoreInteractions(betRepository, kafkaBetProducer, jackpotService);
    }

    @Test
    void placeBet_shouldSaveBetAndSendToKafka_whenKafkaEnabled() {
        when(idempotencyService.isDuplicateRequest(IDEMPOTENCY_KEY)).thenReturn(false);
        when(betRepository.existsById(betEvent.betId())).thenReturn(false);
        when(kafkaProperties.enabled()).thenReturn(true);
        when(kafkaProperties.topic()).thenReturn("jackpot-bets");

        Bet savedBet = Bet.fromEvent(betEvent);
        when(betRepository.save(any(Bet.class))).thenReturn(savedBet);

        Bet result = betService.placeBet(betEvent, IDEMPOTENCY_KEY);

        assertEquals("bet1", result.getBetId());
        verify(betRepository).save(any(Bet.class));
        verify(kafkaBetProducer).sendBetEvent("jackpot-bets", betEvent);
        verify(idempotencyService).markProcessed(IDEMPOTENCY_KEY);
        verifyNoInteractions(jackpotService);
    }

    @Test
    void placeBet_shouldSaveBetAndCallJackpotService_whenKafkaDisabled() {
        when(idempotencyService.isDuplicateRequest(IDEMPOTENCY_KEY)).thenReturn(false);
        when(betRepository.existsById(betEvent.betId())).thenReturn(false);
        when(kafkaProperties.enabled()).thenReturn(false);

        Bet savedBet = Bet.fromEvent(betEvent);
        when(betRepository.save(any(Bet.class))).thenReturn(savedBet);

        Bet result = betService.placeBet(betEvent, IDEMPOTENCY_KEY);

        assertEquals("bet1", result.getBetId());
        verify(betRepository).save(any(Bet.class));
        verify(jackpotService).processBet(any(Bet.class));
        verify(idempotencyService).markProcessed(IDEMPOTENCY_KEY);
        verifyNoInteractions(kafkaBetProducer);
    }

    @Test
    void getAllBets_shouldReturnBetsFromRepository() {
        List<Bet> bets = List.of(Bet.fromEvent(betEvent));
        when(betRepository.findAll()).thenReturn(bets);

        List<Bet> result = betService.getAllBets();

        assertEquals(1, result.size());
        assertEquals("bet1", result.get(0).getBetId());
        verify(betRepository).findAll();
    }

    @Test
    void getBet_shouldReturnBetFromRepository() {
        Bet bet = Bet.fromEvent(betEvent);
        when(betRepository.findById("bet1")).thenReturn(Optional.of(bet));

        Optional<Bet> result = betService.getBet("bet1");

        assertTrue(result.isPresent());
        assertEquals("bet1", result.get().getBetId());
        verify(betRepository).findById("bet1");
    }
}
