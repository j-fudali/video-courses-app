package com.jfudali.coursesapp.question.controller;

import com.jfudali.coursesapp.course.service.CourseService;
import com.jfudali.coursesapp.dto.ResponseMessage;
import com.jfudali.coursesapp.exceptions.NotFoundException;
import com.jfudali.coursesapp.exceptions.OwnershipException;
import com.jfudali.coursesapp.question.dto.AddQuestionsDto;
import com.jfudali.coursesapp.question.dto.UpdateQuestionDto;
import com.jfudali.coursesapp.question.service.QuestionService;
import com.jfudali.coursesapp.quiz.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
@RestController
@RequestMapping("/courses/{courseId}/lessons/{lessonId}/quiz/questions")
@RequiredArgsConstructor
public class QuestionController {
    private final CourseService courseService;
    private final QuestionService questionService;
    @PostMapping
    public ResponseEntity<ResponseMessage> addQuestions(@PathVariable("lessonId") Integer lessonId,
                                                        @RequestBody AddQuestionsDto addQuestionsDto)
            throws NotFoundException, OwnershipException {
        return new ResponseEntity<>(new ResponseMessage(questionService.addQuestions(lessonId,addQuestionsDto)), HttpStatus.OK);
    }
    @PutMapping
    public ResponseEntity<ResponseMessage> updateQuestion(@PathVariable("lessonId") Integer lessonId,
                                                           @RequestBody UpdateQuestionDto updateQuestionDto) throws OwnershipException, NotFoundException {
        return new ResponseEntity<>(new ResponseMessage(questionService.updateQuestion(lessonId, updateQuestionDto)), HttpStatus.OK);
    }
}
