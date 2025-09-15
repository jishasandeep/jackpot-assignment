package com.assignment.jackpot.service;

import com.assignment.jackpot.exception.BetEvaluationException;
import com.assignment.jackpot.model.Bet;
import com.assignment.jackpot.model.Jackpot;
import com.assignment.jackpot.model.JackpotReward;
import com.assignment.jackpot.repository.JackpotRepository;
import com.assignment.jackpot.repository.JackpotRewardRepository;
import com.assignment.jackpot.reward.RewardStrategy;
import com.assignment.jackpot.reward.RewardStrategyFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class RewardService {
    private final JackpotRepository jackpotRepository;
    private final JackpotRewardRepository rewardRepository;

    public void evaluateReward(Bet bet) {
        Jackpot jackpot = jackpotRepository.findById(bet.getJackpotId())
                .orElseThrow(() -> new BetEvaluationException("Jackpot not found"));

        RewardStrategy strategy = RewardStrategyFactory.getStrategy(jackpot.getRewardType());
        try {
            double rewardAmount = strategy.calculateReward(bet, jackpot);
            log.info("Reward amount for bet {} is {}", bet.getBetId(), rewardAmount);

            JackpotReward reward;
            if (rewardAmount > 0) {
                reward = new JackpotReward();
                reward.setBetId(bet.getBetId());
                reward.setUserId(bet.getUserId());
                reward.setJackpotId(jackpot.getId());
                reward.setRewardAmount(rewardAmount);
                reward.setCreatedAt(LocalDateTime.now());
                rewardRepository.save(reward);
                // Reset jackpot pool
                jackpot.setCurrentPool(jackpot.getInitialPool());
                jackpotRepository.save(jackpot);
            }

        }catch (Exception e) {
            throw new BetEvaluationException("Failed to evaluate reward " + bet.getBetId(), e);
        }
    }




}
