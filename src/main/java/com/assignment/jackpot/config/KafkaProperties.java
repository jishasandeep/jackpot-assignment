package com.assignment.jackpot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "kafka")
public record KafkaProperties(boolean enabled,
                              String topic) {
}
