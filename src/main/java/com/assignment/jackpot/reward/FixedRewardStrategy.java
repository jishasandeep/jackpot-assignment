package com.assignment.jackpot.reward;

import com.assignment.jackpot.model.Bet;
import com.assignment.jackpot.model.Jackpot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Random;

@Slf4j
@Component
public class FixedRewardStrategy implements RewardStrategy {
    private final Random random = new Random();


    @Override
    public double calculateReward(Bet bet, Jackpot jackpot) {
        double draw = random.nextDouble();
        log.info("Reward random draw - {}, fixed chance - {}",draw,jackpot.getFixedRewardChance());
        return draw <= jackpot.getFixedRewardChance() ?jackpot.getCurrentPool():0;
    }

}
