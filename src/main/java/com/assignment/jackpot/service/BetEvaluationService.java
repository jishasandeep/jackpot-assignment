package com.assignment.jackpot.service;

import com.assignment.jackpot.dto.BetEvaluationResponse;
import com.assignment.jackpot.exception.BetEvaluationException;
import com.assignment.jackpot.model.Bet;
import com.assignment.jackpot.model.JackpotReward;
import com.assignment.jackpot.repository.BetRepository;
import com.assignment.jackpot.repository.JackpotRewardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BetEvaluationService {
    private final JackpotRewardRepository rewardRepository;
    private final BetRepository betRepository;

    public BetEvaluationResponse getBetEvaluationResult(String betId) {
        if(!betRepository.existsById(betId)) throw new BetEvaluationException("Invalid bet id");

        Optional<JackpotReward> optionalReward = rewardRepository.findById(betId);
        if(optionalReward.isPresent()) {
            JackpotReward reward = optionalReward.get();
            return new BetEvaluationResponse(
                    reward.getBetId(),
                    reward.getUserId(),
                    reward.getJackpotId(),
                    true,
                    reward.getRewardAmount()
            );
        }
        else {
            Bet bet = betRepository.findById(betId).orElseThrow();
            return new BetEvaluationResponse(
                    bet.getBetId(),
                    bet.getUserId(),
                    bet.getJackpotId(),
                    false,
                    0.0
            );
        }
    }


}
