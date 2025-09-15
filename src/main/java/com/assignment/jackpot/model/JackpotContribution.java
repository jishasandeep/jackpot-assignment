package com.assignment.jackpot.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class JackpotContribution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String betId;
    private Long userId;
    private Long jackpotId;
    private Double stakeAmount;
    private Double contributionAmount;
    private Double currentJackpotAmount;
    private LocalDateTime createdAt;
}
