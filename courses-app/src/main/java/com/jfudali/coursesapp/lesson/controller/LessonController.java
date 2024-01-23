package com.jfudali.coursesapp.lesson.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfudali.coursesapp.course.service.CourseService;
import com.jfudali.coursesapp.dto.ResponseMessage;
import com.jfudali.coursesapp.exceptions.NotFoundException;
import com.jfudali.coursesapp.exceptions.OwnershipException;
import com.jfudali.coursesapp.lesson.dto.CreateLessonDto;
import com.jfudali.coursesapp.lesson.dto.LessonDto;
import com.jfudali.coursesapp.lesson.dto.UpdateLessonDto;
import com.jfudali.coursesapp.lesson.model.Lesson;
import com.jfudali.coursesapp.lesson.service.LessonService;
import com.jfudali.coursesapp.quiz.dto.CreateQuizDto;
import com.jfudali.coursesapp.quiz.dto.UpdateQuizDto;
import com.jfudali.coursesapp.quiz.model.Quiz;
import com.jfudali.coursesapp.quiz.service.QuizService;
import com.jfudali.coursesapp.quiz.view.QuizView;
import com.jfudali.coursesapp.user.model.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.Update;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/courses/{courseId}/lessons")
 @Validated
public class LessonController {
    private final LessonService lessonService;
    private final ModelMapper modelMapper;
    @PostMapping()
    public ResponseEntity<Map<String, Integer>> createLesson(@PathVariable(name = "courseId") Integer courseId,
                                            @RequestBody CreateLessonDto createLessonDto)
            throws NotFoundException, OwnershipException {
        Integer lessonId = lessonService.createLesson(courseId, createLessonDto).getIdlesson();
        Map<String , Integer> response = new HashMap<>();
        response.put("lessonId", lessonId);
        return  new ResponseEntity<>(response, HttpStatus.OK);
//        return new ResponseEntity<CreateLessonDto>(
//                modelMapper.map(lessonService.createLesson(courseId, createLessonDto), CreateLessonDto.class),
//                HttpStatus.OK);
    }
    @GetMapping("/{lessonId}")
    public ResponseEntity<LessonDto> getLessonById(@PathVariable("lessonId") Integer lessonId,
                                                   @PathVariable("courseId") Integer courseId,
                                                   Authentication authentication) throws NotFoundException {
        LessonDto lessonDto = modelMapper.map(lessonService.getLessonById(courseId, lessonId), LessonDto.class);
        return new ResponseEntity<>(lessonDto, HttpStatus.OK);
    }

    @PatchMapping("/{lessonId}")
    public  ResponseEntity<UpdateLessonDto> updateLesson(
            @PathVariable("courseId") Integer courseId,@PathVariable("lessonId") Integer lessonId,
            @RequestBody UpdateLessonDto updateLessonDto)
            throws NotFoundException, OwnershipException {
        return new ResponseEntity<>(modelMapper.map(
                lessonService.updateLesson(courseId,lessonId,updateLessonDto), UpdateLessonDto.class), HttpStatus.OK);
    }
    @DeleteMapping("/{lessonId}")
    public  ResponseEntity<ResponseMessage> deleteLesson(@PathVariable("lessonId") Integer lessonId, @PathVariable("courseId") Integer courseId) throws NotFoundException {
        return  new ResponseEntity<>(lessonService.deleteLesson(courseId, lessonId), HttpStatus.OK);
    }

}
