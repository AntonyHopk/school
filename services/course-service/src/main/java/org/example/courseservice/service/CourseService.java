package org.example.courseservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.courseservice.DTO.CourseResponse;
import org.example.courseservice.DTO.CreateCourseRequest;
import org.example.courseservice.DTO.UpdateCourseRequest;
import org.example.courseservice.entity.Course;
import org.example.courseservice.entity.CourseStatus;
import org.example.courseservice.exception.ForbiddenException;
import org.example.courseservice.exception.NotFoundException;
import org.example.courseservice.kafka.CourseEventProducer;
import org.example.courseservice.repository.CourseRepository;
import org.example.courseservice.security.JwtPrincipal;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseEventProducer events;

    @Transactional
    public CourseResponse create(JwtPrincipal principal, CreateCourseRequest req) {
        requireTeacher(principal);
        Course c = new Course();
        c.setOwnerUserId(principal.userId());
        c.setTitle(req.title());
        c.setDescription(req.description());
        c.setCourseStatus(CourseStatus.DRAFT);
        return toRespone(courseRepository.save(c));
    }

    @Transactional
    public CourseResponse get(Long id) {
        Course c = courseRepository.findById(id).orElseThrow(() -> new NotFoundException("Course not found"));
        return toRespone(c);
    }

    @Transactional
    public CourseResponse update(JwtPrincipal principal, Long id, UpdateCourseRequest req) {
        requireTeacher(principal);
        Course c = courseRepository.findById(id).orElseThrow(() -> new NotFoundException("Course not found"));
        requireOwner(principal, c);
        if (req.title() != null) {
            c.setTitle(req.title());
        }
        if (req.description() != null) {
            c.setDescription(req.description());
        }
        return toRespone(courseRepository.save(c));
    }

    @Transactional
    public void delete(JwtPrincipal principal, Long id) {
        requireTeacher(principal);
        Course c = courseRepository.findById(id).orElseThrow(() -> new NotFoundException("Course not found"));
        requireOwner(principal, c);
        courseRepository.delete(c);
    }

    @Transactional
    public CourseResponse publish(JwtPrincipal principal, Long id) {
        requireTeacher(principal);
        Course c = courseRepository.findById(id).orElseThrow(() -> new NotFoundException("Course not found"));
        requireOwner(principal, c);
        if (c.getCourseStatus() == CourseStatus.PUBLISHED) {
            return toRespone(c);
        }
        c.setCourseStatus(CourseStatus.PUBLISHED);
        Course updated = courseRepository.save(c);
        events.coursePublished(updated.getId(), updated.getOwnerUserId(), updated.getTitle());
        return toRespone(updated);

    }


    private void requireOwner(JwtPrincipal principal, Course c) {
        if (!c.getOwnerUserId().equals(principal.userId())) {
            throw new ForbiddenException("Not your course");
        }
    }


    private CourseResponse toRespone(Course save) {
        return new CourseResponse(save.getId(), save.getOwnerUserId(), save.getTitle(), save.getDescription(), save.getCourseStatus(), save.getCreatedAt(), save.getUpdatedAt());
    }

    private void requireTeacher(JwtPrincipal principal) {
        if (!"TEACHER".equalsIgnoreCase(principal.role())) {
            throw new ForbiddenException("Only teacher can perform this action");
        }
    }
}
