package com.jfudali.coursesapp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfudali.coursesapp.errors.ApiError;
import com.jfudali.coursesapp.exceptions.NotFoundException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request,response);
        }
        catch (JwtException ex){
            setErrorResponse(HttpStatus.UNAUTHORIZED, response, ex);
        }
        catch (NotFoundException ex){
            setErrorResponse(HttpStatus.NOT_FOUND, response, ex);
        }
        catch (RuntimeException ex){
            setErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, response, ex);
        }
    }
    private void setErrorResponse(HttpStatus status, HttpServletResponse response, Throwable throwable) throws IOException {
        String error = throwable.getMessage();
        ApiError apiError = new ApiError(status, throwable.getLocalizedMessage(), error);
        response.setStatus(apiError.getStatus().value());
        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(apiError));
    }
}
