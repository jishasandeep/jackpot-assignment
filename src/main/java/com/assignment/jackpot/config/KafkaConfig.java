package com.assignment.jackpot.config;

import com.assignment.jackpot.dto.BetEvent;
import com.assignment.jackpot.service.KafkaBetProducer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
@EnableConfigurationProperties({KafkaProperties.class})
public class KafkaConfig {

    @Bean
    @ConditionalOnProperty(name = "kafka.enabled", havingValue = "true", matchIfMissing = true)
    public KafkaBetProducer kafkaBetProducer(KafkaTemplate<String, BetEvent> kafkaTemplate) {
        return new KafkaBetProducer(kafkaTemplate);
    }
}
