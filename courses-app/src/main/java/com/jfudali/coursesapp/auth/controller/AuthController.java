package com.jfudali.coursesapp.auth.controller;

import com.jfudali.coursesapp.auth.dto.*;
import com.jfudali.coursesapp.dto.ResponseMessage;
import com.jfudali.coursesapp.resetpassword.dto.ResetPasswordConfirmDto;
import com.jfudali.coursesapp.resetpassword.dto.ResetPasswordDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.jfudali.coursesapp.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;

/**
 * AuthController
 */
@RestController
@RequestMapping(value = "/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterRequest registerRequestDto) {
        return ResponseEntity.ok(authService.register(registerRequestDto));

    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody LoginRequest loginRequestDto) {
        return ResponseEntity.ok(authService.login(loginRequestDto));
    }

}