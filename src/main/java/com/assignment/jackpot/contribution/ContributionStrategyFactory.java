package com.assignment.jackpot.contribution;

public class ContributionStrategyFactory {
    public static ContributionStrategy getStrategy(String type){
        return switch (type.toUpperCase()) {
            case "FIXED" -> new FixedContributionStrategy();
            case "VARIABLE" -> new VariableContributionStrategy();
            default -> throw new IllegalArgumentException("Unknown contribution type : " + type);
        };
    }
}
