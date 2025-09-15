package com.assignment.jackpot.service;

import com.assignment.jackpot.model.Bet;
import com.assignment.jackpot.model.Jackpot;
import com.assignment.jackpot.repository.JackpotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class JackpotService {

    private final JackpotRepository jackpotRepository;
    private final ContributionService contributionService;
    private final RewardService rewardService;

    @Transactional
    public void processBet(Bet bet) {
        contributionService.processContribution(bet);
        rewardService.evaluateReward(bet);
        log.info("Bet is processed successfully: betId={}, jackpotId={}",
                bet.getBetId(), bet.getJackpotId());
    }

    public void saveJackpots(List<Jackpot> jackpots){
        jackpots.forEach(jackpotRepository::save);
    }

    public void saveJackpot(Jackpot jackpot) {
        jackpotRepository.save(jackpot);
    }

    public List<Jackpot> getAllJackpots() {
        return jackpotRepository.findAll();
    }

    public Optional<Jackpot> getJackpot(Long id) {
        return jackpotRepository.findById(id);
    }



}
