package com.jfudali.coursesapp.question.service;

import com.jfudali.coursesapp.answer.model.Answer;
import com.jfudali.coursesapp.exceptions.NotFoundException;
import com.jfudali.coursesapp.lesson.model.Lesson;
import com.jfudali.coursesapp.lesson.repository.LessonRepository;
import com.jfudali.coursesapp.lesson.service.LessonService;
import com.jfudali.coursesapp.question.dto.SetQuestionsDto;
import com.jfudali.coursesapp.question.model.Question;
import com.jfudali.coursesapp.quiz.model.Quiz;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final LessonRepository lessonRepository;
    private final LessonService lessonService;
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
