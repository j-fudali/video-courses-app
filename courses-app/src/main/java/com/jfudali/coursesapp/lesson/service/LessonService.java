package com.jfudali.coursesapp.lesson.service;

import com.jfudali.coursesapp.dto.ResponseMessage;
import com.jfudali.coursesapp.exceptions.OwnershipException;
import com.jfudali.coursesapp.file.service.FileService;
import com.jfudali.coursesapp.lesson.dto.UpdateLessonDto;
import com.jfudali.coursesapp.user.model.EUserType;
import com.jfudali.coursesapp.user.model.User;
import com.jfudali.coursesapp.user.repository.UserRepository;
import com.jfudali.coursesapp.user.service.UserService;
import org.springframework.stereotype.Service;

import com.jfudali.coursesapp.course.model.Course;
import com.jfudali.coursesapp.course.repository.CourseRepository;
import com.jfudali.coursesapp.exceptions.NotFoundException;
import com.jfudali.coursesapp.lesson.dto.CreateLessonDto;
import com.jfudali.coursesapp.lesson.model.Lesson;
import com.jfudali.coursesapp.lesson.repository.LessonRepository;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@Data
@RequiredArgsConstructor
public class LessonService {
    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final FileService fileService;
    @Transactional
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
    public Lesson getLessonById(Integer courseId, Integer lessonId) throws NotFoundException {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new NotFoundException("Lesson not found"));
        checkIsLessonPartOfCourse(lesson, courseId);
        return lesson;
    }
    @Transactional
    public void setUserPassedLesson(Lesson lesson, User user){
        lesson.getUsersPassedLesson().add(user);
        user.getPassedLessons().add(lesson);
        userRepository.save(user);
        lessonRepository.save(lesson);
    }
    @Transactional
    public Lesson updateLesson(Integer courseId, Integer lessonId, UpdateLessonDto updateLessonDto) throws NotFoundException, OwnershipException {
        Lesson lesson = this.getLessonById(courseId, lessonId);
        if (updateLessonDto.getTitle() != null)
            lesson.setTitle(updateLessonDto.getTitle());
        if (updateLessonDto.getDescription() != null)
            lesson.setDescription(updateLessonDto.getDescription());
        if (updateLessonDto.getVideo() != null) {
            fileService.deleteFile(lesson.getVideo());
            lesson.setVideo(updateLessonDto.getVideo());
        }
        lessonRepository.save(lesson);
        return lesson;
    }
    @Transactional
        public ResponseMessage deleteLesson (Integer courseId, Integer lessonId) throws NotFoundException {
            Lesson lesson = this.getLessonById(courseId, lessonId);
            if (lesson.getVideo() != null) {
                fileService.deleteFile(lesson.getVideo());
            }
            lessonRepository.deleteById(lessonId);
            return new ResponseMessage("Lesson has been deleted");
        }
        @Transactional
        public void verifyUserCompleteLesson(Integer courseId, Integer lessonId,
                                             String userEmail){
        Lesson lesson = this.getLessonById(courseId, lessonId);
        User user = this.userService.getUserByEmail(userEmail);
            if(user.getType().equals(EUserType.USER) && lesson.getQuiz() == null){
                if(!lesson.getUsersPassedLesson().contains(user)){
                    this.setUserPassedLesson(lesson, user);
                }
            }
        }
        private void checkIsLessonPartOfCourse(Lesson lesson, Integer courseId){
            if(!lesson.getCourse().getIdcourse().equals(courseId)) throw new NotFoundException("Lesson of course with provided id not found");
        }

}
