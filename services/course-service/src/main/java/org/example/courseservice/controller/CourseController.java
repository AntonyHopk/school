package org.example.courseservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.courseservice.DTO.CourseResponse;
import org.example.courseservice.DTO.CreateCourseRequest;
import org.example.courseservice.DTO.UpdateCourseRequest;
import org.example.courseservice.entity.Course;
import org.example.courseservice.security.RequestPrincipalResolver;
import org.example.courseservice.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;
    private final RequestPrincipalResolver principal;

    @PostMapping
    public ResponseEntity<?> createCourse(@RequestHeader("X-User-Id") String userId,
                                          @RequestHeader("X-User-Role") String role,
                                          @Valid @RequestBody CreateCourseRequest course) {

        var princ = principal.fromHeaders(userId, role);
        CourseResponse created = courseService.create(princ, course);
        return ResponseEntity.created(URI.create("/courses/" + created.id())).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCourse(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.get(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@RequestHeader("X-User-Id") String userId,
                                    @RequestHeader("X-User-Role") String role,
                                    @PathVariable Long id,
                                    @Valid @RequestBody UpdateCourseRequest course) {
        var princ = principal.fromHeaders(userId, role);
        return ResponseEntity.ok(courseService.update(princ, id, course));
    }

    @PatchMapping("/{id}/publish")
    public ResponseEntity<?> publish(@RequestHeader("X-User-Id") String userId,
                                     @RequestHeader("X-User-Role") String role,
                                     @PathVariable Long id) {
        var princ = principal.fromHeaders(userId, role);
        return ResponseEntity.ok(courseService.publish(princ, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(@RequestHeader("X-User-Id") String userId,
                                          @RequestHeader("X-User-Role") String role,
                                          @PathVariable Long id) {
        var princ = principal.fromHeaders(userId, role);
        courseService.delete(princ, id);
        return ResponseEntity.noContent().build();
    }
}
