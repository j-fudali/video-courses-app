package com.jfudali.coursesapp.quiz.controller;

import com.jfudali.coursesapp.exceptions.NotFoundException;
import com.jfudali.coursesapp.quiz.dto.CreateQuizRequest;
import com.jfudali.coursesapp.quiz.model.Quiz;
import com.jfudali.coursesapp.quiz.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/courses/{courseId}/lessons/{lessonId}/quiz")
@RequiredArgsConstructor
public class QuizController {
    private final QuizService quizService;

    @PostMapping
    public ResponseEntity<Quiz> createQuiz(@PathVariable("lessonId") Integer lessonId, @RequestBody CreateQuizRequest createQuizRequest) throws NotFoundException {
        return  new ResponseEntity<>(quizService.createQuiz(lessonId,createQuizRequest), HttpStatus.OK);
    }
}
