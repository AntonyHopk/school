package org.example.courseservice.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CourseEventProducer {
    public static final String TOPIC = "course.published.v1";
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void coursePublished(Long courseId,Long ownerUserId, String title){
        var event = new CoursePublishedEvent(
                UUID.randomUUID(),
                TOPIC,
                Instant.now(),
                "course-service",
                new CoursePublishedEvent.Payload(courseId,ownerUserId,title)
        );
        kafkaTemplate.send(TOPIC,courseId.toString(), event);
    }

}
