package com.jfudali.coursesapp.lesson.controller;

import com.jfudali.coursesapp.course.service.CourseService;
import com.jfudali.coursesapp.dto.ResponseMessage;
import com.jfudali.coursesapp.exceptions.NotFoundException;
import com.jfudali.coursesapp.exceptions.OwnershipException;
import com.jfudali.coursesapp.lesson.dto.CreateLessonDto;
import com.jfudali.coursesapp.lesson.dto.UpdateLessonDto;
import com.jfudali.coursesapp.lesson.model.Lesson;
import com.jfudali.coursesapp.lesson.service.LessonService;
import com.jfudali.coursesapp.quiz.dto.CreateQuizDto;
import com.jfudali.coursesapp.quiz.dto.UpdateQuizDto;
import com.jfudali.coursesapp.quiz.model.Quiz;
import com.jfudali.coursesapp.quiz.service.QuizService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.Update;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/courses/{courseId}/lessons")
 @Validated
public class LessonController {
    private final LessonService lessonService;
    private final ModelMapper modelMapper;

    @PostMapping()
    public ResponseEntity<CreateLessonDto> createLesson(@PathVariable(name = "courseId") Integer courseId,
            @RequestBody CreateLessonDto createLessonDto, Principal principal)
            throws NotFoundException, OwnershipException {
        return new ResponseEntity<CreateLessonDto>(
                modelMapper.map(lessonService.createLesson(courseId, createLessonDto), CreateLessonDto.class),
                HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getLessonById(@PathVariable("id") Integer id,
                                           @PathVariable("courseId") String courseId) throws NotFoundException {
        return new ResponseEntity<>(lessonService.getLessonById(id), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public  ResponseEntity<UpdateLessonDto> updateLesson(
            @PathVariable("courseId") Integer courseId,@PathVariable("id") Integer id,
            @RequestBody UpdateLessonDto updateLessonDto, Principal principal)
            throws NotFoundException, OwnershipException {
        return new ResponseEntity<>(modelMapper.map(
                lessonService.updateLesson(id,updateLessonDto), UpdateLessonDto.class), HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public  ResponseEntity<ResponseMessage> deleteLesson(@PathVariable("id") Integer id) throws NotFoundException {
        return  new ResponseEntity<>(lessonService.deleteLesson(id), HttpStatus.OK);
    }

}
