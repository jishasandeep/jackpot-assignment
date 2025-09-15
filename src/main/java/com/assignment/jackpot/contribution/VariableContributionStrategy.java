package com.assignment.jackpot.contribution;

import com.assignment.jackpot.model.Jackpot;
import org.springframework.stereotype.Component;

@Component
public class VariableContributionStrategy implements ContributionStrategy{

    @Override
    public double calculateContribution(double betAmount, Jackpot jackpot)  {
        double effectivePercentage = Math.max(
                0.01,
                jackpot.getInitialContributionPercentage()) -
                (jackpot.getContributionDecayRate() * (jackpot.getCurrentPool() / jackpot.getMaxPool())
        );
        return betAmount * effectivePercentage;
    }
}
