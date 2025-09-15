package com.assignment.jackpot.contribution;

import com.assignment.jackpot.model.Jackpot;
import org.springframework.stereotype.Component;

@Component
public class FixedContributionStrategy implements ContributionStrategy{
    @Override
    public double calculateContribution(double betAmount, Jackpot jackpot)  {
        return betAmount*jackpot.getFixedContributionPercentage();
    }
}
