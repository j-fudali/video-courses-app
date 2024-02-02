package com.jfudali.coursesapp.question.service;

import com.jfudali.coursesapp.exceptions.NotFoundException;
import com.jfudali.coursesapp.lesson.model.Lesson;
import com.jfudali.coursesapp.lesson.repository.LessonRepository;
import com.jfudali.coursesapp.lesson.service.LessonService;
import com.jfudali.coursesapp.question.dto.SetQuestionsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final LessonRepository lessonRepository;
    private final LessonService lessonService;
    @Transactional

    public String setQuestions(Integer courseId, Integer lessonId, SetQuestionsDto setQuestionsDto){
        Lesson lesson = lessonService.getLessonById(courseId, lessonId);
        if(lesson.getQuiz() == null) throw  new NotFoundException("Quiz not found");
        if(!lesson.getQuiz().getQuestions().isEmpty()){
            lesson.getQuiz().getQuestions().clear();
        }
        lesson.getQuiz().getQuestions().addAll(setQuestionsDto.getQuestions());
        lessonRepository.save(lesson);
        return "Questions has been updated";
    }
}
