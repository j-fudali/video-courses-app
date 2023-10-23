package com.jfudali.coursesapp.auth.service;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.jfudali.coursesapp.auth.model.AuthenticationResponse;
import com.jfudali.coursesapp.auth.model.LoginRequest;
import com.jfudali.coursesapp.auth.model.RegisterRequest;
import com.jfudali.coursesapp.config.JwtService;
import com.jfudali.coursesapp.user.model.EUserType;
import com.jfudali.coursesapp.user.model.User;
import com.jfudali.coursesapp.user.repository.UserRepository;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest registerRequest) {
        User user;
        try {
            user = User.builder()
                    .firstname(registerRequest.getFirstname())
                    .lastname(registerRequest.getLastname())
                    .email(registerRequest.getEmail())
                    .password(passwordEncoder.encode(registerRequest.getPassword()))
                    .type(EUserType.User)
                    .build();
            userRepository.save(user);

        } catch (DataAccessException ex) {
            if (ex.getCause() instanceof ConstraintViolationException)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "E-mail already exists");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot create new user");
        }
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            var user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow();
            var jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder().token(jwtToken).build();
        } catch (AuthenticationException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Access denied");
        } catch (ExpiredJwtException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token expired");
        }
    }
}
