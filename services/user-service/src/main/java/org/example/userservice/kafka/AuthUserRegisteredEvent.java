package org.example.userservice.kafka;

import java.time.Instant;
import java.util.UUID;

public record AuthUserRegisteredEvent(
        UUID eventId,
        String eventType,
        Instant occurredAt,
        String producer,
        AuthUserRegisteredPayload payload
) {
}
