package com.jfudali.coursesapp.lesson.controller;

import com.jfudali.coursesapp.course.service.CourseService;
import com.jfudali.coursesapp.dto.ResponseMessage;
import com.jfudali.coursesapp.exceptions.NotFoundException;
import com.jfudali.coursesapp.exceptions.OwnershipException;
import com.jfudali.coursesapp.lesson.dto.CreateLessonDto;
import com.jfudali.coursesapp.lesson.dto.LessonDto;
import com.jfudali.coursesapp.lesson.dto.UpdateLessonDto;
import com.jfudali.coursesapp.lesson.model.Lesson;
import com.jfudali.coursesapp.lesson.service.LessonService;
import com.jfudali.coursesapp.quiz.dto.QuizDto;
import com.jfudali.coursesapp.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/courses/{courseId}/lessons")
public class LessonController {
    private final LessonService lessonService;
    private final ModelMapper modelMapper;
    private final CourseService courseService;
    @PostMapping()
    public ResponseEntity<Map<String, Integer>> createLesson(@PathVariable(name = "courseId") Integer courseId,
                                           @Valid @RequestBody CreateLessonDto createLessonDto)
            throws NotFoundException, OwnershipException {
        Integer lessonId = lessonService.createLesson(courseId, createLessonDto).getIdlesson();
        Map<String , Integer> response = new HashMap<>();
        response.put("lessonId", lessonId);
        return  new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/{lessonId}")
    public ResponseEntity<LessonDto> getLessonById(@PathVariable("lessonId") Integer lessonId,
                                                   @PathVariable("courseId") Integer courseId,
                                                   Principal principal) throws NotFoundException {
        Lesson lesson = lessonService.getLessonById(courseId, lessonId);
        this.lessonService.verifyUserCompleteLesson(courseId, lessonId,
                                                    principal.getName());
        this.courseService.verifyUserPassedCourse(courseId, principal.getName());
        LessonDto lessonDto = modelMapper.map(lesson, LessonDto.class);
        if(lesson.getQuiz() != null){
        lessonDto.setQuiz(new QuizDto(lesson.getQuiz().getTitle(),
                                      lesson.getQuiz().getQuestions(),
                                      lesson.getQuiz().getExaminee().stream().anyMatch(user -> user.getEmail().equals(principal.getName()))));
        }
        return new ResponseEntity<>(lessonDto, HttpStatus.OK);
    }

    @PatchMapping("/{lessonId}")
    public  ResponseEntity<UpdateLessonDto> updateLesson(
            @PathVariable("courseId") Integer courseId,@PathVariable("lessonId") Integer lessonId,
            @Valid @RequestBody UpdateLessonDto updateLessonDto)
            throws NotFoundException, OwnershipException {
        return new ResponseEntity<>(modelMapper.map(
                lessonService.updateLesson(courseId,lessonId,updateLessonDto), UpdateLessonDto.class), HttpStatus.OK);
    }
    @DeleteMapping("/{lessonId}")
    public  ResponseEntity<ResponseMessage> deleteLesson(@PathVariable("lessonId") Integer lessonId, @PathVariable("courseId") Integer courseId) throws NotFoundException {
        return  new ResponseEntity<>(lessonService.deleteLesson(courseId, lessonId), HttpStatus.OK);
    }

}
