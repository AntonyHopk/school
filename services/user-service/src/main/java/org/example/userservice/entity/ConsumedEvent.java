package org.example.userservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "consumed_events")
public class ConsumedEvent {
    @Id
    @Column(name="event_id",nullable = false)
    private UUID id;

    @Column(name = "event_type",nullable = false,length = 100)
    private String eventType;

    @Column(name = "consumed_at",nullable = false)
    private Instant consumedAt = Instant.now();

}
