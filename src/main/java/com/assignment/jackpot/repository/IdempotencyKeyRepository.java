package com.assignment.jackpot.repository;

import com.assignment.jackpot.model.IdempotencyKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdempotencyKeyRepository  extends JpaRepository<IdempotencyKey,String> {
}
