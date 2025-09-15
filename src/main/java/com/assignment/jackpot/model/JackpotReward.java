package com.assignment.jackpot.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class JackpotReward {
    @Id
    private String betId;
    private Long userId;
    private Long jackpotId;
    private Double rewardAmount;
    private LocalDateTime createdAt;
}
