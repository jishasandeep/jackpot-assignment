package com.assignment.jackpot.dto;

public record BetEvaluationResponse(String betId,Long userId,Long jackpotId,boolean won,double rewardAmount) {
}
