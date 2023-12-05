package com.jfudali.coursesapp.errors;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.jfudali.coursesapp.exceptions.AlreadyExistsException;
import com.jfudali.coursesapp.exceptions.FileException;
import com.jfudali.coursesapp.exceptions.NotFoundException;
import com.jfudali.coursesapp.exceptions.OwnershipException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.persistence.EntityExistsException;

@ControllerAdvice
public class GlobalErrorHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ApiError apiError = new ApiError(HttpStatus.METHOD_NOT_ALLOWED, ex.getLocalizedMessage(), ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Request is missing a body", "Required request body is missing");
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    private ResponseEntity<ApiError> getApiErrorResponse(HttpStatus status, Throwable throwable){
        String error = throwable.getMessage();
        ApiError apiError = new ApiError(status, throwable.getLocalizedMessage(), error);
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
    @ExceptionHandler({ResponseStatusException.class})
    public ResponseEntity<ApiError> handleResponseStatusException(ResponseStatusException ex){
        String error = ex.getReason();
        ApiError apiError = new ApiError(HttpStatus.valueOf(ex.getStatusCode().value()), ex.getLocalizedMessage(), error);
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
    @ExceptionHandler({AlreadyExistsException.class, EntityExistsException.class})
    public  ResponseEntity<ApiError> handleAlreadyExistsException(Exception ex){
        return getApiErrorResponse(HttpStatus.CONFLICT, ex);
    }
    @ExceptionHandler({ AccessDeniedException.class, OwnershipException.class })
    public ResponseEntity<ApiError> handleForbiddenError(Exception ex) {
        return getApiErrorResponse(HttpStatus.FORBIDDEN, ex);
    }
    @ExceptionHandler({ AuthenticationException.class })
    public ResponseEntity<ApiError> handleAuthenticationException(Exception ex) {
        return  getApiErrorResponse(HttpStatus.UNAUTHORIZED, ex);
    }
    @ExceptionHandler({SdkClientException.class, AmazonServiceException.class, FileException.class,SQLIntegrityConstraintViolationException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ApiError> handleBadRequestError(Exception ex) {
        return getApiErrorResponse(HttpStatus.BAD_REQUEST, ex);
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDaoExceptions(DataIntegrityViolationException ex){
        String error = ((ConstraintViolationException)ex.getCause()).getConstraintName();
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Unhandled DAO exception", "Unhandled DAO exception");
        if (error.endsWith("email_UNIQUE")){
            apiError = new ApiError(HttpStatus.CONFLICT, "E-mail is already used", "E-mail is already used");
        }
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
    @Override
    @Nullable
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<String> errors = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Invalid data provided", errors);
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }



    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFoundException(Exception ex) {
        return getApiErrorResponse(HttpStatus.NOT_FOUND, ex);
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAll(Exception ex) {
        ApiError apiError = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), "Error occurred");
        return new ResponseEntity<Object>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }
}
