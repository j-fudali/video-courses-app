package com.jfudali.coursesapp.config.authorization;

import com.jfudali.coursesapp.course.service.CourseService;
import com.jfudali.coursesapp.exceptions.NotFoundException;
import com.jfudali.coursesapp.user.model.User;
import com.jfudali.coursesapp.user.service.UserService;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.function.Supplier;
@Component
public class CourseOwnerAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
    private final UserService userService;
    public CourseOwnerAuthorizationManager(UserService userService){
        this.userService = userService;
    }
    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        User user = userService.getUserByEmail(authentication.get().getName());
        return new AuthorizationDecision(user.getCourses().stream().anyMatch(course -> Objects.equals(course.getIdcourse(), Integer.valueOf(object.getVariables().get("courseId")))));
    }
}
