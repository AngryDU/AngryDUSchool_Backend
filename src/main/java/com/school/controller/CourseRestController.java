package com.school.controller;

import com.school.entity.Course;
import com.school.service.api.CourseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/courses")
public class CourseRestController implements GlobalController{

    private final CourseService courseService;

    public CourseRestController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Course create(@RequestBody Course course) {
        return courseService.create(course);
    }

    @GetMapping
    public Page<Course> getAll(Pageable pageable) {
        return courseService.getAll(pageable);
    }

    @GetMapping("/{id}")
    public Course getById(@PathVariable UUID id) {
        return courseService.getById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        courseService.delete(id);
    }
}
