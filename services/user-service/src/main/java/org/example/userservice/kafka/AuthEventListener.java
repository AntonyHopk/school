package org.example.userservice.kafka;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.userservice.entity.ConsumedEvent;
import org.example.userservice.entity.UserProfile;
import org.example.userservice.repository.ConsumedEventRepository;
import org.example.userservice.repository.UserProfileRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
@RequiredArgsConstructor
public class AuthEventListener {
    private final UserProfileRepository profileRepo;
    private final ConsumedEventRepository consumedRepo;
    private static final String TOPIC = "auth.user.registered.v1";

    @Transactional
    @KafkaListener(
            topics = TOPIC,
            containerFactory = "userRegisteredKafkaListenerContainerFactory"
    )
    public void onUserRegistered(EventEnvelope<AuthUserRegisteredPayload> envelope) {
        if (consumedRepo.existsById(envelope.eventId())) {
            return;
        }
        var p = envelope.payload();

        if (!profileRepo.existsById(p.userId())) {
            UserProfile profile = new UserProfile();
            profile.setId(p.userId());
            String base = p.email().split("@")[0].toLowerCase();
            profile.setUsername(pickUniqueUsername(base, p.userId()));
            profileRepo.save(profile);
        }

        ConsumedEvent consumedEvent = new ConsumedEvent();
        consumedEvent.setId(envelope.eventId());
        consumedEvent.setEventType(envelope.eventType());
        consumedRepo.save(consumedEvent);
    }

    private String pickUniqueUsername(String base, Long userId) {
        String candidate = base;
        if (!profileRepo.existsByUsername(candidate)) {
            return candidate;
        }
        candidate = base + userId;
        if (!profileRepo.existsByUsername(candidate))
            return candidate;
        return "user" + userId;
    }
}
