package com.assignment.jackpot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Jackpot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double initialPool;
    private Double currentPool;
    private Double maxPool;
    private String contributionType; // FIXED or VARIABLE
    private Double fixedContributionPercentage;
    private Double initialContributionPercentage;
    private Double contributionDecayRate;
    private String rewardType; // FIXED or VARIABLE
    private Double fixedRewardChance;
    @Version
    private Long version;
}
