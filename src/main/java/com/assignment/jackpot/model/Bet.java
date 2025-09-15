package com.assignment.jackpot.model;

import com.assignment.jackpot.dto.BetEvent;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Bet {
    @Id
    private String betId;
    private Long userId;
    private Long jackpotId;
    private Double betAmount;
    private LocalDateTime createdAt;

    public static Bet fromEvent(BetEvent event) {
        Bet bet = new Bet();
        bet.setBetId(event.betId());
        bet.setUserId(event.userId());
        bet.setJackpotId(event.jackpotId());
        bet.setBetAmount(event.betAmount());
        bet.setCreatedAt(LocalDateTime.now());
        return bet;
    }


}
