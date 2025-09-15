package com.assignment.jackpot.contribution;

import com.assignment.jackpot.model.Jackpot;

public interface ContributionStrategy  {
    double calculateContribution(double betAmount, Jackpot jackpot);
}

