package com.jfudali.coursesapp.exceptions;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.jfudali.coursesapp.errors.ApiError;

import io.jsonwebtoken.security.SignatureException;
import jakarta.persistence.EntityExistsException;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity<ApiError> handleAccessDeniedException(Exception ex) {
        String error = ex.getMessage();
        ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, ex.getLocalizedMessage(), error);
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
    @ExceptionHandler({ AuthenticationException.class })
    public ResponseEntity<ApiError> handleAuthenticationException(Exception ex) {
        String error = ex.getMessage();
        ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED, ex.getLocalizedMessage(), error);
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleNullPointerException(MethodArgumentTypeMismatchException ex) {
        String error = ex.getMessage();
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
        return new ResponseEntity<ApiError>(apiError, apiError.getStatus());
    }

    @Override
    @Nullable
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<String> errors = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(OwnershipException.class)
    public ResponseEntity<ApiError> handleOwnershipException(OwnershipException ex) {
        String error = ex.getMessage();
        ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, ex.getLocalizedMessage(), error);
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFoundException(NotFoundException ex, WebRequest request) {
        String error = ex.getMessage();
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), error);
        return new ResponseEntity<ApiError>(apiError, apiError.getStatus());
    }

    @ExceptionHandler({SQLIntegrityConstraintViolationException.class, DataIntegrityViolationException.class})
    public ResponseEntity<ApiError> handleDataAccessException(SQLIntegrityConstraintViolationException ex,
            WebRequest request) {
        String error = ex.getMessage();
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException ex,
            WebRequest request) {
        List<String> errors = new ArrayList<>();
        ex.getConstraintViolations().forEach(cv -> errors.add(cv.getMessage()));
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        return new ResponseEntity<ApiError>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<ApiError> handlEntityExistsException(EntityExistsException ex) {
        String error = ex.getMessage();
        ApiError apiError = new ApiError(HttpStatus.CONFLICT, ex.getLocalizedMessage(), error);
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        ApiError apiError = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), "Error occurred");
        return new ResponseEntity<Object>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }
}
