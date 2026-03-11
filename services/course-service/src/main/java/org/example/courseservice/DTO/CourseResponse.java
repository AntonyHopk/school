package org.example.courseservice.DTO;

import org.example.courseservice.entity.CourseStatus;

import java.time.Instant;

public record CourseResponse(Long id, Long OwnerUserId, String title, String description, CourseStatus status,
                             Instant createdAt, Instant updatedAt) {
}
