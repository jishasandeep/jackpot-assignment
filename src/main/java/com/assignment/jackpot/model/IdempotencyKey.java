package com.assignment.jackpot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class IdempotencyKey {
    @Id
    private String idempotencyKey;
    private LocalDateTime createdAt = LocalDateTime.now();
}