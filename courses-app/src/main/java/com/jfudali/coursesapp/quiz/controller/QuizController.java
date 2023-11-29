package com.jfudali.coursesapp.quiz.controller;

import com.jfudali.coursesapp.exceptions.NotFoundException;
import com.jfudali.coursesapp.exceptions.OwnershipException;
import com.jfudali.coursesapp.quiz.dto.CreateQuizDto;
import com.jfudali.coursesapp.quiz.dto.UpdateQuizDto;
import com.jfudali.coursesapp.quiz.model.Quiz;
import com.jfudali.coursesapp.quiz.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/courses/{courseId}/lessons/{lessonId}/quiz")
@RequiredArgsConstructor
public class QuizController {
    private final QuizService quizService;
    @PostMapping()
    public ResponseEntity<Quiz> createQuiz(@PathVariable("courseId") Integer courseId,
                                           @PathVariable("lessonId") Integer lessonId,
                                           @RequestBody CreateQuizDto createQuizDto,
                                           Principal principal) throws NotFoundException, OwnershipException {
        return  new ResponseEntity<>(quizService.createQuiz(lessonId, createQuizDto), HttpStatus.OK);
    }
    @PatchMapping
    public ResponseEntity<Quiz> updateQuiz(@PathVariable("courseId") Integer courseId,
                                           @PathVariable("lessonId") Integer lessonId,
                                           @RequestBody UpdateQuizDto updateQuizDto,
                                           Principal principal) throws NotFoundException, OwnershipException {
        return new ResponseEntity<>(quizService.updateQuiz(lessonId, updateQuizDto), HttpStatus.OK);
    }
    @DeleteMapping
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteQuiz(@PathVariable("courseId") Integer courseId,
                           @PathVariable("lessonId") Integer lessonId,
                           Principal principal) throws OwnershipException, NotFoundException {
        quizService.deleteQuiz(lessonId);
    }

}
