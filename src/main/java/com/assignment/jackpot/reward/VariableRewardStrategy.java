package com.assignment.jackpot.reward;

import com.assignment.jackpot.model.Bet;
import com.assignment.jackpot.model.Jackpot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Random;

@Slf4j
@Component
public class VariableRewardStrategy implements RewardStrategy {
    private final Random random;

    public VariableRewardStrategy(Random random) {
        this.random = random;
    }

    public VariableRewardStrategy() {
        this(new Random());
    }

    @Override
    public double calculateReward(Bet bet, Jackpot jackpot) {
        double poolRatio = jackpot.getCurrentPool() / jackpot.getMaxPool();
        double chance = Math.min(1.0, poolRatio); // chance grows as pool grows
        double draw = random.nextDouble();
        log.info("Reward random draw - {}, variable chance - {}",draw,chance);
        return draw <= chance ? jackpot.getCurrentPool() : 0;
    }


}
