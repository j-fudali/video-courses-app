package com.jfudali.coursesapp.user.controller;

import java.security.Principal;

import com.jfudali.coursesapp.dto.ResponseMessage;
import com.jfudali.coursesapp.user.dto.ChangePasswordDto;
import com.jfudali.coursesapp.user.dto.GetUserDto;
import com.jfudali.coursesapp.user.dto.UpdateUserProfileDto;
import jakarta.validation.Valid;
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
@RequestMapping(value = "/users/me")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private  final ModelMapper modelMapper;
    @GetMapping
    public ResponseEntity<GetUserDto> getUserProfile(Principal principal){
        return new ResponseEntity<>(modelMapper.map(userService.getUserByEmail(principal.getName()), GetUserDto.class), HttpStatus.OK);
    }
    @PatchMapping
    public ResponseEntity<ResponseMessage> updateUserProfile(@Valid @RequestBody UpdateUserProfileDto updateUserProfileDto, Principal principal){
        return new ResponseEntity<>(new ResponseMessage(userService.updateUser(updateUserProfileDto.getFirstname(), updateUserProfileDto.getLastname(), principal.getName())),
                                    HttpStatus.OK);
    }
    @PostMapping("/change-password")
    public ResponseEntity<ResponseMessage> changePassword(@Valid @RequestBody ChangePasswordDto changePasswordDto, Principal principal ){
        return new ResponseEntity<>(new ResponseMessage(userService.changePassword(changePasswordDto.getOldPassword(),changePasswordDto.getNewPassword(), principal.getName())), HttpStatus.OK);
    }
    @GetMapping(value = "/courses")
    public ResponseEntity<Page<?>> getCurrentUserOwnedCourses(@RequestParam(name = "type") String type, Principal principal, Pageable pageable)
            throws NotFoundException {
        return new ResponseEntity<>(userService.getCurrentUserCourses(type, principal.getName(), pageable), HttpStatus.OK);
    }
}
