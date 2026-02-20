package org.example.userservice.config;

import org.example.userservice.kafka.AuthUserRegisteredPayload;
import org.example.userservice.kafka.EventEnvelope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;

@Configuration
public class KafkaConfig {

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, EventEnvelope<AuthUserRegisteredPayload>> kafkaListenerContainerFactory(
            ConsumerFactory<String, EventEnvelope<AuthUserRegisteredPayload>> consumerFactory) {

        var factory = new ConcurrentKafkaListenerContainerFactory<String, EventEnvelope<AuthUserRegisteredPayload>>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
}
