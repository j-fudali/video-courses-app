package com.jfudali.coursesapp.course.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.jfudali.coursesapp.category.model.Category;
import com.jfudali.coursesapp.category.repository.CategoryRepository;
import com.jfudali.coursesapp.course.model.Course;
import com.jfudali.coursesapp.course.model.CreateCourseRequest;
import com.jfudali.coursesapp.course.model.ECourseOwnership;
import com.jfudali.coursesapp.course.repository.CourseRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;

    public Course createCourse(CreateCourseRequest createCourseRequest) {
        Course course = Course.builder()
                .name(createCourseRequest.getName())
                .cost(createCourseRequest.getCost())
                .category(categoryRepository.findById(createCourseRequest.getCategoryId())
                        .orElseThrow(() -> new EntityNotFoundException("Category not found")))
                .build();
        return courseRepository.save(course);
    }

    public List<Course> getAllCourses(Map<String, String> params) {
        // if (params.containsKey("name"))
        // return courseRepository.findByName(params.get("name"));
        return courseRepository.findAll();
    }
}
