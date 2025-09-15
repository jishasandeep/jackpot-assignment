package com.assignment.jackpot.exception;

public class BetEvaluationException extends RuntimeException {
    public BetEvaluationException(String message) {
        super(message);
    }

    public BetEvaluationException(String message, Throwable cause) {
        super(message, cause);
    }
}

