package org.example.courseservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.courseservice.DTO.CourseResponse;
import org.example.courseservice.DTO.CreateCourseRequest;
import org.example.courseservice.DTO.UpdateCourseRequest;
import org.example.courseservice.entity.Course;
import org.example.courseservice.security.JwtPrincipalExtractor;
import org.example.courseservice.security.RequestPrincipalResolver;
import org.example.courseservice.service.CourseService;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;
    private final JwtPrincipalExtractor principal;

    @PostMapping
    public ResponseEntity<?> createCourse(@AuthenticationPrincipal Jwt userPrincipal, @Valid @RequestBody CreateCourseRequest course) {
        var princ = principal.fromJwt(userPrincipal);
        CourseResponse created = courseService.create(princ, course);
        return ResponseEntity.created(URI.create("/courses/" + created.id())).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCourse(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.get(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@AuthenticationPrincipal Jwt userPrincipal, @PathVariable Long id, @Valid @RequestBody UpdateCourseRequest course) {
        var princ = principal.fromJwt(userPrincipal);
        return ResponseEntity.ok(courseService.update(princ, id, course));
    }

    @PatchMapping("/{id}/publish")
    public ResponseEntity<?> publish(@AuthenticationPrincipal Jwt userPrincipal, @PathVariable Long id) {
        var princ = principal.fromJwt(userPrincipal);
        return ResponseEntity.ok(courseService.publish(princ, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(@AuthenticationPrincipal Jwt userPrincipal, @PathVariable Long id) {
        var princ = principal.fromJwt(userPrincipal);
        courseService.delete(princ, id);
        return ResponseEntity.noContent().build();
    }
}
