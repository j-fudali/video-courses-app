package com.jfudali.coursesapp.config.authorization;

import com.jfudali.coursesapp.course.model.Course;
import com.jfudali.coursesapp.course.service.CourseService;
import com.jfudali.coursesapp.exceptions.NotFoundException;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public final class CourseCreatorAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
    private final CourseService courseService;
    public CourseCreatorAuthorizationManager(CourseService courseService){
        this.courseService = courseService;
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        Course course = courseService.getCourseById(Integer.valueOf(object.getVariables().get("courseId")));
        return new AuthorizationDecision(course.getCreator().getEmail().equals(authentication.get().getName()));
    }
}
