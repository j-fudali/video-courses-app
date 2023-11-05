package com.jfudali.coursesapp.auth.controller;

import org.springframework.web.bind.annotation.RestController;
import com.jfudali.coursesapp.auth.model.AuthenticationResponse;
import com.jfudali.coursesapp.auth.model.LoginRequest;
import com.jfudali.coursesapp.auth.model.RegisterRequest;
import com.jfudali.coursesapp.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * AuthController
 */
@RestController
@RequestMapping(value = "/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid RegisterRequest registerRequestDto) {
        return ResponseEntity.ok(authService.register(registerRequestDto));

    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid LoginRequest loginRequestDto) {
        return ResponseEntity.ok(authService.login(loginRequestDto));
    }

}