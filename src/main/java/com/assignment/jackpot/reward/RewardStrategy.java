package com.assignment.jackpot.reward;

import com.assignment.jackpot.model.Bet;
import com.assignment.jackpot.model.Jackpot;

import java.math.BigDecimal;

public interface RewardStrategy {
    //public boolean isWinner(double currentJackpotAmount);
    public double calculateReward(Bet bet, Jackpot jackpot);
}

;