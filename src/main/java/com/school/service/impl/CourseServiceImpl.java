package com.school.service.impl;

import com.school.entity.Course;
import com.school.exception.ExceptionLocations;
import com.school.exception.CustomException;
import com.school.message.InternalizationMessageManagerConfig;
import com.school.repository.CourseRepository;
import com.school.service.api.CourseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service(value = "courseService")
public class CourseServiceImpl implements CourseService {
    public static final String KEY_FOR_EXCEPTION_COURSE_NOT_FOUND = "CourseService.CourseNotFound";
    private final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public Course getById(UUID id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new CustomException(InternalizationMessageManagerConfig
                        .getExceptionMessage(KEY_FOR_EXCEPTION_COURSE_NOT_FOUND),
                        ExceptionLocations.ORDER_SERVICE_NOT_FOUND));
    }

    @Override
    public Page<Course> getAll(Pageable pageable) {
        return courseRepository.findAll(pageable);
    }

    @Override
    public Course create(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public void delete(UUID uuid) {

    }
}
