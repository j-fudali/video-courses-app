package com.jfudali.coursesapp.lesson.service;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.jfudali.coursesapp.course.model.Course;
import com.jfudali.coursesapp.course.repository.CourseRepository;
import com.jfudali.coursesapp.exceptions.NotFoundException;
import com.jfudali.coursesapp.lesson.dto.CreateLessonRequest;
import com.jfudali.coursesapp.lesson.model.Lesson;
import com.jfudali.coursesapp.lesson.repository.LessonRepository;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Service
@Data
@RequiredArgsConstructor
public class LessonService {
    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;

    public Page<Lesson> findAllByCourseId(Integer courseId, Pageable pageable) {
        return lessonRepository.findLessonsByCourseIdcourse(courseId, pageable);
    }

    public Lesson createLesson(Integer courseId, CreateLessonRequest createLessonRequest) throws NotFoundException {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found"));
        Lesson lesson = Lesson.builder().title(createLessonRequest.getTitle())
                .description(createLessonRequest.getDescription()).video(createLessonRequest.getVideo()).course(course)
                .build();
        lessonRepository.save(lesson);
        return lesson;
    }
}
