package org.example.courseservice.kafka;

import java.time.Instant;
import java.util.UUID;

public record CoursePublishedEvent(
        UUID eventId,
        String eventType,
        Instant occurredAt,
        String producer,
        Payload payload

) {
    public record Payload(Long courseId, Long ownerUserId, String title) {
    }
}
