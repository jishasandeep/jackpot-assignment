package com.assignment.jackpot.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record BetEvent(
        @NotNull(message = "Bet ID is required")
        @NotEmpty(message = "Bet ID is required")
        String betId,

        @NotNull(message = "UserId cannot be null")
        Long userId,

        @NotNull(message = "JackpotId cannot be null")
        Long jackpotId,

        @Min(value = 1, message = "Bet amount must be at least 1")
        double betAmount
) {}
