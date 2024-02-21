package com.jfudali.coursesapp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfudali.coursesapp.errors.ApiError;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;
import java.util.Objects;

import static org.springframework.security.authorization.AuthenticatedAuthorizationManager.authenticated;
import static org.springframework.security.authorization.AuthorityAuthorizationManager.hasAuthority;
import static org.springframework.security.authorization.AuthorizationManagers.allOf;
import static org.springframework.security.authorization.AuthorizationManagers.anyOf;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
        private final JwtAuthenticationFilter jwtAuthFilter;
        private final AuthenticationProvider authenticationProvider;
        private final ExceptionHandlerFilter exceptionHandlerFilter;
        @Qualifier("delegatedAuthenticationEntryPoint")
        private final AuthenticationEntryPoint authenticationEntryPoint;
        private final Environment environment;


        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                       @Qualifier("courseCreatorAuthorizationManager") AuthorizationManager<RequestAuthorizationContext> courseCreatorAuthz,
                                                       @Qualifier("courseOwnerAuthorizationManager") AuthorizationManager<RequestAuthorizationContext> courseOwnerAuthz) throws Exception {
                http
                        .cors((cors) -> cors.configurationSource(corsConfigurationSource())
                        )
                        .csrf(AbstractHttpConfigurer::disable)
                                .authorizeHttpRequests(
                                                (authorizeHttpRequests) -> {
                                                    authorizeHttpRequests
                                                            .requestMatchers(

                                                                            "/**","/auth" +
                                                                            "/**", "/reset-password/**", "/error", "/categories")
                                                            .permitAll()
                                                            .requestMatchers(HttpMethod.GET, "/courses", "/courses/{courseId}")
                                                            .permitAll()
                                                            .requestMatchers("/courses/{id}/buy")
                                                            .authenticated()
                                                            .requestMatchers(HttpMethod.POST, "/courses/{courseId}/lessons/{lessonId}/quiz/verify")
                                                            .access(allOf(authenticated(), courseOwnerAuthz))
                                                            .requestMatchers(HttpMethod.GET, "/courses/{courseId}/**")
                                                            .access(allOf(authenticated(), anyOf(courseOwnerAuthz, courseCreatorAuthz)))
                                                            .requestMatchers("/courses/{courseId}/**")
                                                            .access(allOf(authenticated(), hasAuthority("ADMIN"), courseCreatorAuthz))
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
        @Bean
        CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration cors = new CorsConfiguration();
                cors.setAllowedOrigins(
                        List.of(Objects.requireNonNull(environment.getProperty(
                                "DOMAIN"))));
                cors.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                cors.setAllowedHeaders(List.of("*"));
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", cors);
                return source;
        }


}
