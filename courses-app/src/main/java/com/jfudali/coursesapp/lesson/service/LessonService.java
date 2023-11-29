package com.jfudali.coursesapp.lesson.service;

import com.jfudali.coursesapp.dto.ResponseMessage;
import com.jfudali.coursesapp.exceptions.OwnershipException;
import com.jfudali.coursesapp.file.service.FileService;
import com.jfudali.coursesapp.lesson.dto.UpdateLessonDto;
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

@Service
@Data
@RequiredArgsConstructor
public class LessonService {
    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final FileService fileService;
    public Lesson createLesson(Integer courseId, CreateLessonDto createLessonDto) throws NotFoundException, OwnershipException {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found"));
        Lesson lesson = Lesson.builder().title(createLessonDto.getTitle())
                .description(createLessonDto.getDescription()).video(createLessonDto.getVideo()).course(course)
                .build();
        course.getLessons().add(lesson);
        courseRepository.save(course);
        return lesson;
    }
    public Lesson getLessonById(Integer lessonId) throws NotFoundException {
        return lessonRepository.findById(lessonId).orElseThrow(() -> new NotFoundException("Lesson not found"));
    }
    public Lesson updateLesson(Integer lessonId, UpdateLessonDto updateLessonDto) throws NotFoundException, OwnershipException {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new NotFoundException("Lesson not found"));
        if(updateLessonDto.getTitle() != null)
            lesson.setTitle(updateLessonDto.getTitle());
        if(updateLessonDto.getDescription() != null)
            lesson.setDescription(updateLessonDto.getDescription());
        if(updateLessonDto.getVideo() != null){
            //TODO
            //Not working
            fileService.deleteFile(lesson.getVideo());
            lesson.setVideo(updateLessonDto.getVideo());
        }
        return lesson;
    }
    public ResponseMessage deleteLesson(Integer lessonId) throws NotFoundException {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new NotFoundException("Lesson not found"));
        if(lesson.getVideo() != null){
            //TODO
            //Not working
            fileService.deleteFile(lesson.getVideo());
        }
        lessonRepository.deleteById(lessonId);
        return  new ResponseMessage("Lesson has been deleted");
    }

}
