package com.assignment.jackpot.contribution;

import com.assignment.jackpot.model.Jackpot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FixedContributionStrategyTest {

    private FixedContributionStrategy strategy;
    private Jackpot jackpot;

    @BeforeEach
    void setUp() {
        strategy = new FixedContributionStrategy();
        jackpot = new Jackpot();
    }

    @Test
    void shouldReturnCorrectContribution_whenValidInputs() {
        jackpot.setFixedContributionPercentage(0.1); // 10%
        double betAmount = 100.0;

        double contribution = strategy.calculateContribution(betAmount, jackpot);

        assertThat(contribution).isEqualTo(10.0);
    }

    @Test
    void shouldReturnZeroContribution_whenPercentageIsZero() {
        jackpot.setFixedContributionPercentage(0.0);
        double betAmount = 100.0;

        double contribution = strategy.calculateContribution(betAmount, jackpot);

        assertThat(contribution).isEqualTo(0.0);
    }

    @Test
    void shouldHandleLargeBetAmount() {
        jackpot.setFixedContributionPercentage(0.2); // 20%
        double betAmount = 1_000_000.0;

        double contribution = strategy.calculateContribution(betAmount, jackpot);

        assertThat(contribution).isEqualTo(200_000.0);
    }

    @Test
    void shouldHandleNegativeBetAmount() {
        jackpot.setFixedContributionPercentage(0.1);
        double betAmount = -100.0;

        double contribution = strategy.calculateContribution(betAmount, jackpot);

        assertThat(contribution).isEqualTo(-10.0);
    }
}
