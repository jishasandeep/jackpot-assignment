package com.assignment.jackpot.service;

import com.assignment.jackpot.config.KafkaProperties;
import com.assignment.jackpot.dto.BetEvent;
import com.assignment.jackpot.model.Bet;
import com.assignment.jackpot.repository.BetRepository;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BetService {

    private final BetRepository betRepository;
    private final KafkaBetProducer kafkaBetProducer;
    private final JackpotService jackpotService;
    private final KafkaProperties kafkaProperties;
    private final IdempotencyService idempotencyService;


    @Transactional
    public Bet placeBet(BetEvent betEvent, String idempotencyKey) {
        if (idempotencyService.isDuplicateRequest(idempotencyKey)) {
            throw new DuplicateRequestException("Duplicate request with idempotency key: " + idempotencyKey);
        } else if (betRepository.existsById(betEvent.betId())) {
            throw new DuplicateRequestException("Duplicate request with same bet id: " + betEvent.betId());
        }
        Bet bet = Bet.fromEvent(betEvent);
        betRepository.save(bet);


        log.info("New Bet {} is saved for jackpot {}",bet.getBetId(),bet.getJackpotId());
        if(kafkaProperties.enabled()) {
            kafkaBetProducer.sendBetEvent(kafkaProperties.topic(),betEvent);
        }else {
            log.info("Kafka disabled. Logging the bet event : {}", betEvent);
            jackpotService.processBet(bet);
        }
        idempotencyService.markProcessed(idempotencyKey);
        return bet;
    }





    public List<Bet> getAllBets() {
        return betRepository.findAll();
    }

    public Optional <Bet>getBet(String id) {
        return betRepository.findById(id);
    }
}
