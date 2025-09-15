package com.assignment.jackpot.service;

import com.assignment.jackpot.model.IdempotencyKey;
import com.assignment.jackpot.repository.IdempotencyKeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class IdempotencyService {
    private final IdempotencyKeyRepository repository;

    public boolean isDuplicateRequest(String key) {
        return repository.existsById(key);
    }

    public void markProcessed(String key) {
        repository.save(new IdempotencyKey(key, LocalDateTime.now()));
    }
}
