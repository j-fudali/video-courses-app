package com.jfudali.coursesapp.quiz.service;

import com.jfudali.coursesapp.exceptions.NotFoundException;
import com.jfudali.coursesapp.lesson.repository.LessonRepository;
import com.jfudali.coursesapp.quiz.dto.CreateQuizRequest;
import com.jfudali.coursesapp.quiz.model.Quiz;
import com.jfudali.coursesapp.quiz.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuizService {
    private final  QuizRepository quizRepository;
    private final LessonRepository lessonRepository;
    public Quiz createQuiz(Integer lessonId, CreateQuizRequest createQuizRequest) throws NotFoundException {
        Quiz quiz = Quiz.builder().title(createQuizRequest.getTitle()).lesson(lessonRepository.findById(lessonId).orElseThrow(() -> new NotFoundException("Lesson not found"))).build();
        return quizRepository.save(quiz);
    }
}
