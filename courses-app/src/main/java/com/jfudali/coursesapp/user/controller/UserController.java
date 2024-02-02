package com.jfudali.coursesapp.user.controller;

import java.security.Principal;

import com.jfudali.coursesapp.dto.ResponseMessage;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.jfudali.coursesapp.exceptions.NotFoundException;
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
    public ResponseEntity<Page<?>> getCurrentUserOwnedCourses(@RequestParam(name = "type") String type, Principal principal, Pageable pageable)
            throws NotFoundException {
        return new ResponseEntity<>(userService.getCurrentUserCourses(type, principal.getName(), pageable), HttpStatus.OK);
    }
    @GetMapping(value = "/me/courses/{courseId}")
    public ResponseEntity<ResponseMessage> checkHasBoughtOrIsCreator(@PathVariable("courseId") Integer courseId, Principal principal){
        return new ResponseEntity<>(userService.checkHasBought(courseId, principal.getName()), HttpStatus.OK);
    }

}
