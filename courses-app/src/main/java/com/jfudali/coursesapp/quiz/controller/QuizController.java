package com.jfudali.coursesapp.quiz.controller;

import com.jfudali.coursesapp.exceptions.NotFoundException;
import com.jfudali.coursesapp.quiz.dto.CreateQuizDto;
import com.jfudali.coursesapp.quiz.dto.UpdateQuizDto;
import com.jfudali.coursesapp.quiz.dto.UserCompletedQuizDto;
import com.jfudali.coursesapp.quiz.dto.VerifyQuizDto;
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
                                           @RequestBody CreateQuizDto createQuizDto
                                           ) throws NotFoundException {
        return  new ResponseEntity<>(quizService.createQuiz(courseId, lessonId, createQuizDto), HttpStatus.OK);
    }
    @PatchMapping
    public ResponseEntity<Quiz> updateQuiz(@PathVariable("courseId") Integer courseId,
                                           @PathVariable("lessonId") Integer lessonId,
                                           @RequestBody UpdateQuizDto updateQuizDto
                                           ) throws NotFoundException  {
        return new ResponseEntity<>(quizService.updateQuiz(courseId, lessonId, updateQuizDto), HttpStatus.OK);
    }
    @DeleteMapping
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteQuiz(@PathVariable("courseId") Integer courseId,
                           @PathVariable("lessonId") Integer lessonId
                           ) throws NotFoundException {
        quizService.deleteQuiz(courseId,lessonId);
    }
    @PostMapping("/verify")
    public ResponseEntity<VerifyQuizDto> verifyUserAnswers(@RequestBody UserCompletedQuizDto userCompletedQuizDto,
                                                           @PathVariable Integer courseId,
                                                           @PathVariable Integer lessonId,
                                                           Principal principal){
        return new ResponseEntity<>(new VerifyQuizDto(quizService.checkUserPassedQuiz(courseId, lessonId, userCompletedQuizDto, principal.getName())), HttpStatus.OK);
    }

}
