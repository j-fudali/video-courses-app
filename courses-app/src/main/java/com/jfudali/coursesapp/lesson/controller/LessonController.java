package com.jfudali.coursesapp.lesson.controller;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jfudali.coursesapp.exceptions.NotFoundException;
import com.jfudali.coursesapp.lesson.dto.CreateLessonRequest;
import com.jfudali.coursesapp.lesson.dto.CreateLessonResponse;
import com.jfudali.coursesapp.lesson.model.Lesson;
import com.jfudali.coursesapp.lesson.service.LessonService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/courses/{courseId}/lessons")
 @Validated
public class LessonController {
    private final LessonService lessonService;
    private final ModelMapper modelMapper;

    @GetMapping()
    public ResponseEntity<Page<Lesson>> getAllLessonsByCourseId(@PathVariable(name = "courseId") Integer courseId,
            Pageable pageable) {
        return new ResponseEntity<Page<Lesson>>(lessonService.findAllByCourseId(courseId, pageable),
                HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<CreateLessonResponse> createLesson(@PathVariable(name = "courseId") Integer courseId,
            @RequestBody @Valid CreateLessonRequest createLessonRequest)
            throws NotFoundException {
        return new ResponseEntity<CreateLessonResponse>(
                modelMapper.map(lessonService.createLesson(courseId, createLessonRequest), CreateLessonResponse.class),
                HttpStatus.OK);
    }
}
