package com.assignment.jackpot.reward;

public class RewardStrategyFactory {
    public static RewardStrategy getStrategy(String type){
        return switch (type.toUpperCase()) {
            case "FIXED" -> new FixedRewardStrategy();
            case "VARIABLE" -> new VariableRewardStrategy();
            default -> throw new IllegalArgumentException("Unknown reward type : " + type);
        };
    }
}
