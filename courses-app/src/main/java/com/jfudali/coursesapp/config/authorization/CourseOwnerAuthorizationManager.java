package com.jfudali.coursesapp.config.authorization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfudali.coursesapp.quiz.view.QuizView;
import com.jfudali.coursesapp.user.model.User;
import com.jfudali.coursesapp.user.service.UserService;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;
@Component
public class CourseOwnerAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
    private final UserService userService;
    private final ObjectMapper objectMapper;
    public CourseOwnerAuthorizationManager(UserService userService, ObjectMapper objectMapper){
        this.userService = userService;
        this.objectMapper = objectMapper;
    }
    @Override
    @Transactional
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        User user = userService.getUserByEmail(authentication.get().getName());
        objectMapper.setConfig(objectMapper.getSerializationConfig().withView(QuizView.Public.class));
        Integer courseId = Integer.valueOf(object.getVariables().get("courseId"));
        return new AuthorizationDecision(user.getOwnerships().stream().anyMatch(ownership -> ownership.getCourse().getIdcourse().equals(courseId)));
    }
}
