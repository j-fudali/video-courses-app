package com.jfudali.coursesapp.question.controller;

import com.jfudali.coursesapp.dto.ResponseMessage;
import com.jfudali.coursesapp.exceptions.NotFoundException;
import com.jfudali.coursesapp.question.dto.SetQuestionsDto;
import com.jfudali.coursesapp.question.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/courses/{courseId}/lessons/{lessonId}/quiz" +
        "/questions")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;
    @PutMapping
    public ResponseEntity<ResponseMessage> setQuestions(@PathVariable("courseId") Integer courseId,
                                                        @PathVariable("lessonId") Integer lessonId,
                                                        @Valid @RequestBody SetQuestionsDto setQuestionsDto) throws NotFoundException {
        return new ResponseEntity<>(new ResponseMessage(questionService.setQuestions(courseId, lessonId, setQuestionsDto)), HttpStatus.OK);
    }
}
