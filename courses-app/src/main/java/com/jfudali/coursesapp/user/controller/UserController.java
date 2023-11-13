package com.jfudali.coursesapp.user.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jfudali.coursesapp.exceptions.NotFoundException;
import com.jfudali.coursesapp.user.dto.AddCourseToOwnedCoursesRequest;
import com.jfudali.coursesapp.user.dto.OwnedCoursesResponse;
import com.jfudali.coursesapp.user.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;

    @GetMapping(value = "/me/courses")
    public ResponseEntity<Page<OwnedCoursesResponse>> getCurrentUserOwnedCourses(Principal principal, Pageable pageable)
            throws NotFoundException {
        return new ResponseEntity<>(userService.getCurrentUserOwnedCourses(principal.getName(), pageable),
                HttpStatus.OK);
    }

    @PostMapping(value = "/me/courses/buy")
    public ResponseEntity<?> addCourseToOwnedCourses(@RequestBody AddCourseToOwnedCoursesRequest body,
            Principal principal)
            throws NotFoundException {
        String result = userService.addCourseToOwnedCourses(principal.getName(), body.getCourseId());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
