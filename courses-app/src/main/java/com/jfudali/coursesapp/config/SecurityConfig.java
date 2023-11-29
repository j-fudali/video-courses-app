package com.jfudali.coursesapp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfudali.coursesapp.errors.ApiError;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import lombok.RequiredArgsConstructor;

import static org.springframework.security.authorization.AuthorityAuthorizationManager.hasAuthority;
import static org.springframework.security.authorization.AuthorizationManagers.allOf;

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
        public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                       @Qualifier("courseCreatorAuthorizationManager") AuthorizationManager<RequestAuthorizationContext> courseCreatorAuthz,
                                                       @Qualifier("courseOwnerAuthorizationManager") AuthorizationManager<RequestAuthorizationContext> courseOwnerAuthz) throws Exception {
                http
                                .csrf(AbstractHttpConfigurer::disable)
                                .authorizeHttpRequests(
                                                (authorizeHttpRequests) -> {
                                                    authorizeHttpRequests
                                                            .requestMatchers("/auth/**", "/error")
                                                            .permitAll()
                                                            .requestMatchers(HttpMethod.GET, "/courses/")
                                                            .permitAll()
                                                            .requestMatchers(HttpMethod.GET, "/courses/{courseId}")
                                                            .permitAll()
                                                            .requestMatchers("/courses/{id}/buy")
                                                            .authenticated()
                                                            .requestMatchers(HttpMethod.GET, "/courses/{courseId}/**")
                                                            .access(courseOwnerAuthz)
                                                            .requestMatchers("/courses/{courseId}/**")
                                                            .access(allOf(courseCreatorAuthz, hasAuthority("ADMIN")))
                                                            .anyRequest()
                                                            .authenticated();
                                                })
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
                                                .authenticationEntryPoint(authenticationEntryPoint)
                                                .accessDeniedHandler(customAccessDeniedHandler()));
                return http.build();
        }
        @Bean
        AccessDeniedHandler customAccessDeniedHandler() {
                return (request, response, ex) -> {
                        String error = ex.getMessage();
                        ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, ex.getLocalizedMessage(), error);
                        response.setStatus(apiError.getStatus().value());
                        response.setContentType("application/json");
                        response.getWriter().write(new ObjectMapper().writeValueAsString(apiError));
                };
        }

}
