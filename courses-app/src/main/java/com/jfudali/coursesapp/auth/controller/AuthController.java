package com.jfudali.coursesapp.auth.controller;

import org.springframework.web.bind.annotation.RestController;
import com.jfudali.coursesapp.auth.model.AuthenticationResponse;
import com.jfudali.coursesapp.auth.model.LoginRequest;
import com.jfudali.coursesapp.auth.model.RegisterRequest;
import com.jfudali.coursesapp.auth.service.AuthService;
import com.jfudali.coursesapp.user.model.User;
import com.jfudali.coursesapp.user.repository.UserRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    private final UserRepository repository;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid RegisterRequest registerRequestDto) {
        return ResponseEntity.ok(authService.register(registerRequestDto));

    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid LoginRequest loginRequestDto) {
        return ResponseEntity.ok(authService.login(loginRequestDto));
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAll() {
        try {
            List<User> items = new ArrayList<User>();

            repository.findAll().forEach(items::add);

            if (items.isEmpty())
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(items, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}