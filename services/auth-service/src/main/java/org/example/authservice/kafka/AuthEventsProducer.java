package org.example.authservice.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AuthEventsProducer {
    public static final String TOPIC_USER_REGISTERED = "auth.user.registered.v1";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void userRegistered(Long userId, String email, String role) {
        var event = new EventEnvelope<>(
                UUID.randomUUID(),
                TOPIC_USER_REGISTERED,
                Instant.now(),
                "auth-service",
                new AuthUserRegisteredPayload(userId, email, role)
        );
        kafkaTemplate.send(TOPIC_USER_REGISTERED, userId.toString(), event);
    }
}
