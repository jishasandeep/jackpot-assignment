package com.assignment.jackpot.controller;

import com.assignment.jackpot.dto.BetEvaluationResponse;
import com.assignment.jackpot.dto.BetEvent;
import com.assignment.jackpot.model.Bet;
import com.assignment.jackpot.service.BetEvaluationService;
import com.assignment.jackpot.service.BetService;
import com.sun.jdi.request.DuplicateRequestException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bets")
@RequiredArgsConstructor
public class BetController {

    private final BetService betService;
    private final BetEvaluationService betEvaluationService;

    @PostMapping
    public ResponseEntity<String> publishBet(@Valid @RequestBody BetEvent betEvent,
                                             @RequestHeader(value = "Idempotency-key")
                                             String idempotencyKey) {
        try {
            Bet savedBet = betService.placeBet(betEvent, idempotencyKey);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .header(HttpHeaders.LOCATION, "/api/bets/" + savedBet.getBetId())
                    .header("Idempotency-key",idempotencyKey)
                    .body("Bet is published to kafka");


        }catch (DuplicateRequestException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .header("Idempotency-key")
                    .body("Duplicate request - Bet was already processed.");
        }catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing the bet."+ex.getMessage());

        }

    }
    @PostMapping("/{betId}/evaluate")
    public ResponseEntity<BetEvaluationResponse> evaluateBet(@PathVariable String betId){
        return ResponseEntity.ok(betEvaluationService.getBetEvaluationResult(betId));
    }

    @GetMapping
    public List<Bet> getAllBets(){
        return betService.getAllBets();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bet> getBet(@PathVariable String id){
        return betService.getBet(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
