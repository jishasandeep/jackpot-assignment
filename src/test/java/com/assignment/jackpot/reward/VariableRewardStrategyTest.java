package com.assignment.jackpot.reward;

import com.assignment.jackpot.model.Bet;
import com.assignment.jackpot.model.Jackpot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class VariableRewardStrategyTest {

    private Jackpot jackpot;
    private Bet bet;

    @BeforeEach
    void setUp() {

        jackpot = new Jackpot();
        jackpot.setId(555L);
        jackpot.setMaxPool(100.0);
        jackpot.setCurrentPool(50.0);

        bet = new Bet();
        bet.setBetId("bet123");
        bet.setUserId(10001L);
        bet.setJackpotId(555L);
        bet.setBetAmount(20.0);
    }

    @Test
    void calculateReward_shouldReturnPoolAmount_whenDrawBelowChance() {
        // given chance = currentPool/maxPool = 0.5
        // mock Random to return 0.3 (below chance)
        Random mockRandom = mock(Random.class);
        when(mockRandom.nextDouble()).thenReturn(0.3);

        VariableRewardStrategy strategy = new VariableRewardStrategy(mockRandom);

        double reward = strategy.calculateReward(bet, jackpot);

        assertEquals(50.0, reward, 0.001);
    }

    @Test
    void calculateReward_shouldReturnZero_whenDrawAboveChance() {
        // chance = 0.5, draw = 0.8 (above chance)
        Random mockRandom = mock(Random.class);
        when(mockRandom.nextDouble()).thenReturn(0.8);

        VariableRewardStrategy strategy = new VariableRewardStrategy(mockRandom);

        double reward = strategy.calculateReward(bet, jackpot);

        assertEquals(0.0, reward, 0.001);
    }

    @Test
    void calculateReward_shouldCapChanceAtOne() {
        jackpot.setCurrentPool(200.0); // poolRatio = 200/100 = 2.0 -> capped to 1.0

        Random mockRandom = mock(Random.class);
        when(mockRandom.nextDouble()).thenReturn(0.9);

        VariableRewardStrategy strategy = new VariableRewardStrategy(mockRandom);
        double reward = strategy.calculateReward(bet, jackpot);
        assertEquals(200.0, reward, 0.001);
    }
}
