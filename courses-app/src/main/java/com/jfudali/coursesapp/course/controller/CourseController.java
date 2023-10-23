package com.jfudali.coursesapp.course.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.jfudali.coursesapp.course.model.Course;
import com.jfudali.coursesapp.course.model.CreateCourseRequest;
import com.jfudali.coursesapp.course.model.ECourseOwnership;
import com.jfudali.coursesapp.course.service.CourseService;
import com.jfudali.coursesapp.user.model.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @PostMapping()
    public ResponseEntity<Course> createCourse(@RequestBody @Valid CreateCourseRequest createCourseRequest) {
        return new ResponseEntity<Course>(courseService.createCourse(createCourseRequest), HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<Course>> getAllCourses(Map<String, String> params,
            @CurrentSecurityContext(expression = "authentication") Authentication authentication) {
        String user = authentication.getName();
        System.out.println(user);
        return ResponseEntity.ok(new ArrayList());
    }
}
