package com.jfudali.coursesapp.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
        private final JwtAuthenticationFilter jwtAuthFilter;
        private final AuthenticationProvider authenticationProvider;
        private final ExceptionHandlerFilter exceptionHandlerFilter;
        @Qualifier("delegatedAuthenticationEntryPoint")
        private final AuthenticationEntryPoint authenticationEntryPoint;
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(csrf -> csrf.disable())
                                .authorizeHttpRequests(
                                                (authorizeHttpRequests) -> authorizeHttpRequests
                                                                .requestMatchers("/auth/**", "/error")
                                                                .permitAll()
                                                                .requestMatchers(HttpMethod.GET, "/courses/**")
                                                                .permitAll()
                                                                .anyRequest()
                                                                .authenticated())
                                .sessionManagement(
                                                (sessionManagement) -> sessionManagement
                                                                .sessionCreationPolicy(
                                                                                SessionCreationPolicy.STATELESS))
                                .authenticationProvider(authenticationProvider)
                                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                                .addFilterBefore(exceptionHandlerFilter, JwtAuthenticationFilter.class)
                                .exceptionHandling(
                                        httpSecurityExceptionHandlingConfigurer ->
                                        httpSecurityExceptionHandlingConfigurer
                                                .authenticationEntryPoint(authenticationEntryPoint));
                return http.build();
        }

}
