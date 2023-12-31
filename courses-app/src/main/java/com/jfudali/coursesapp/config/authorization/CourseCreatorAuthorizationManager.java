package com.jfudali.coursesapp.config.authorization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfudali.coursesapp.course.model.Course;
import com.jfudali.coursesapp.course.service.CourseService;
import com.jfudali.coursesapp.quiz.view.QuizView;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public final class CourseCreatorAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
    private final CourseService courseService;
    private final ObjectMapper objectMapper;
    public CourseCreatorAuthorizationManager(CourseService courseService, ObjectMapper objectMapper){
        this.courseService = courseService;
        this.objectMapper = objectMapper;
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        Course course = courseService.getCourseById(Integer.valueOf(object.getVariables().get("courseId")));
        objectMapper.setConfig(objectMapper.getSerializationConfig().withView(QuizView.Creator.class));
        return new AuthorizationDecision(course.getCreator().getEmail().equals(authentication.get().getName()));
    }
}
