package com.assignment.jackpot.service;

import com.assignment.jackpot.contribution.ContributionStrategy;
import com.assignment.jackpot.contribution.ContributionStrategyFactory;
import com.assignment.jackpot.contribution.FixedContributionStrategy;
import com.assignment.jackpot.model.Bet;
import com.assignment.jackpot.model.Jackpot;
import com.assignment.jackpot.model.JackpotContribution;
import com.assignment.jackpot.repository.JackpotContributionRepository;
import com.assignment.jackpot.repository.JackpotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ContributionServiceTest {

    private JackpotRepository jackpotRepository;
    private JackpotContributionRepository jackpotContributionRepository;
    private ContributionService contributionService;

    private Bet bet;
    private Jackpot jackpot;

    @BeforeEach
    void setUp() {
        jackpotRepository = mock(JackpotRepository.class);
        jackpotContributionRepository = mock(JackpotContributionRepository.class);

        contributionService = new ContributionService(jackpotRepository, jackpotContributionRepository);

        bet = new Bet();
        bet.setBetId("bet123");
        bet.setUserId(5001L);
        bet.setJackpotId(2L);
        bet.setBetAmount(50.0);

        jackpot = new Jackpot();
        jackpot.setId(2L);
        jackpot.setContributionType("FIXED");
        jackpot.setCurrentPool(100.0);
        jackpot.setFixedContributionPercentage(0.1);
    }

    @Test
    void processContribution_shouldUpdateJackpotAndSaveContribution() {

        when(jackpotRepository.findById(2L)).thenReturn(Optional.of(jackpot));


        ContributionStrategy strategy = mock(FixedContributionStrategy.class);
        when(strategy.calculateContribution(50.0, jackpot)).thenReturn(5.0);

        try (MockedStatic<ContributionStrategyFactory> mockedFactory = mockStatic(ContributionStrategyFactory.class)) {
            mockedFactory.when(() -> ContributionStrategyFactory.getStrategy("FIXED")).thenReturn(strategy);

            contributionService.processContribution(bet);

            // jackpot update verified
            assertEquals(105.0, jackpot.getCurrentPool());
            verify(jackpotRepository).save(jackpot);

            // contribution record verified
            ArgumentCaptor<JackpotContribution> captor = ArgumentCaptor.forClass(JackpotContribution.class);
            verify(jackpotContributionRepository).save(captor.capture());
            JackpotContribution savedContribution = captor.getValue();

            assertEquals("bet123", savedContribution.getBetId());
            assertEquals(5001L, savedContribution.getUserId());
            assertEquals(2L, savedContribution.getJackpotId());
            assertEquals(50.0, savedContribution.getStakeAmount());
            assertEquals(5.0, savedContribution.getContributionAmount());
            assertEquals(105.0, savedContribution.getCurrentJackpotAmount());
            assertNotNull(savedContribution.getCreatedAt());
        }
    }

    @Test
    void processContribution_shouldThrowException_whenJackpotNotFound() {
        when(jackpotRepository.findById(2L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> contributionService.processContribution(bet)
        );

        assertEquals("Invalid jackpot ID", exception.getMessage());
        verifyNoInteractions(jackpotContributionRepository);
    }
}
