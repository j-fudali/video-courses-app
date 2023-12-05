package com.jfudali.coursesapp.user.controller;

import java.security.Principal;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jfudali.coursesapp.exceptions.NotFoundException;
import com.jfudali.coursesapp.user.dto.OwnedCoursesDto;
import com.jfudali.coursesapp.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;
    private  final ModelMapper modelMapper;
    @GetMapping(value = "/me/courses")
    public ResponseEntity<Page<?>> getCurrentUserOwnedCourses(@RequestParam(name = "type", required = false) String type, Principal principal, Pageable pageable)
            throws NotFoundException {
        return new ResponseEntity<>(userService.getCurrentUserCourses(type, principal.getName(), pageable), HttpStatus.OK);
    }



}
