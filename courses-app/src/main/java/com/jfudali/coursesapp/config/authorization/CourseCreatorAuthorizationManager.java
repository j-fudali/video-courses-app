package com.jfudali.coursesapp.config.authorization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfudali.coursesapp.course.model.Course;
import com.jfudali.coursesapp.course.repository.CourseRepository;
import com.jfudali.coursesapp.exceptions.NotFoundException;
import com.jfudali.coursesapp.quiz.view.QuizView;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public final class CourseCreatorAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
    private final CourseRepository courseRepository;
    private final ObjectMapper objectMapper;
    public CourseCreatorAuthorizationManager(CourseRepository courseRepository, ObjectMapper objectMapper){
        this.courseRepository = courseRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        Course course =
                courseRepository.findById(Integer.valueOf(object.getVariables().get("courseId"))).orElseThrow(() -> new NotFoundException("Course not found"));
        objectMapper.setConfig(objectMapper.getSerializationConfig().withView(QuizView.Creator.class));
        return new AuthorizationDecision(course.getCreator().getEmail().equals(authentication.get().getName()));
    }
}
