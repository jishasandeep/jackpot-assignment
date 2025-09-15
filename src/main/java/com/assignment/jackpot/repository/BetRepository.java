package com.assignment.jackpot.repository;

import com.assignment.jackpot.model.Bet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BetRepository extends JpaRepository<Bet, String> {
}
