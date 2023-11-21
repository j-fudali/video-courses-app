package com.jfudali.coursesapp.lesson.service;

import com.jfudali.coursesapp.exceptions.OwnershipException;
import com.jfudali.coursesapp.lesson.dto.UpdateLessonDto;
import com.jfudali.coursesapp.user.model.User;
import com.jfudali.coursesapp.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import com.jfudali.coursesapp.course.model.Course;
import com.jfudali.coursesapp.course.repository.CourseRepository;
import com.jfudali.coursesapp.exceptions.NotFoundException;
import com.jfudali.coursesapp.lesson.dto.CreateLessonDto;
import com.jfudali.coursesapp.lesson.model.Lesson;
import com.jfudali.coursesapp.lesson.repository.LessonRepository;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Data
@RequiredArgsConstructor
public class LessonService {
    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    public Lesson createLesson(Integer courseId, CreateLessonDto createLessonDto) throws NotFoundException {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found"));
        Lesson lesson = Lesson.builder().title(createLessonDto.getTitle())
                .description(createLessonDto.getDescription()).video(createLessonDto.getVideo()).course(course)
                .build();
        lessonRepository.save(lesson);
        return lesson;
    }
    public Lesson getLessonById(Integer lessonId) throws NotFoundException {
        return lessonRepository.findById(lessonId).orElseThrow(() -> new NotFoundException("Lesson not found"));
    }
    public Lesson updateLesson(String userEmail,Integer courseId, Integer lessonId, UpdateLessonDto updateLessonDto) throws NotFoundException, OwnershipException {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new NotFoundException("User not found"));
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new NotFoundException("Lesson not found"));
        System.out.println(user.getCourses().stream().noneMatch(course -> Objects.equals(course.getIdcourse(), courseId)));
        if(user.getCourses().stream().noneMatch(course -> Objects.equals(course.getIdcourse(), courseId)))
            throw new OwnershipException("You are not an onwer of this course");
        if(updateLessonDto.getTitle() != null)
            lesson.setTitle(updateLessonDto.getTitle());
        if(updateLessonDto.getDescription() != null)
            lesson.setDescription(updateLessonDto.getDescription());
        if(updateLessonDto.getVideo() != null)
            lesson.setVideo(updateLessonDto.getVideo());
        return lesson;
    }
}
