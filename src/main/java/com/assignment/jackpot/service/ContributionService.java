package com.assignment.jackpot.service;

import com.assignment.jackpot.contribution.ContributionStrategy;
import com.assignment.jackpot.contribution.ContributionStrategyFactory;
import com.assignment.jackpot.model.Bet;
import com.assignment.jackpot.model.Jackpot;
import com.assignment.jackpot.model.JackpotContribution;
import com.assignment.jackpot.repository.JackpotContributionRepository;
import com.assignment.jackpot.repository.JackpotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContributionService {

    private final JackpotRepository jackpotRepository;
    private final JackpotContributionRepository jackpotContributionRepository;

    @Transactional
    public void processContribution(Bet bet) {

        Jackpot jackpot = jackpotRepository.findById(bet.getJackpotId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid jackpot ID"));

        ContributionStrategy strategy = ContributionStrategyFactory.getStrategy(jackpot.getContributionType());
        double contribution = strategy.calculateContribution(bet.getBetAmount(), jackpot);
        log.info("Calculated contribution is {}",contribution);

        jackpot.setCurrentPool(jackpot.getCurrentPool()+ contribution);
        jackpotRepository.save(jackpot);

        //Save contribution record
        JackpotContribution jc = new JackpotContribution();
        jc.setBetId(bet.getBetId());
        jc.setUserId(bet.getUserId());
        jc.setJackpotId(jackpot.getId());
        jc.setStakeAmount(bet.getBetAmount());
        jc.setContributionAmount(contribution);
        jc.setCurrentJackpotAmount(jackpot.getCurrentPool());
        jc.setCreatedAt(LocalDateTime.now());
        jackpotContributionRepository.save(jc);

        log.info("Contribution is processed successfully: betId={}, jackpotId={}, contribution={}",
                bet.getBetId(), jackpot.getId(), contribution);
    }


}
