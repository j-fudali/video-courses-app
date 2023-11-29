package com.jfudali.coursesapp.quiz.service;

import com.jfudali.coursesapp.exceptions.AlreadyExistsException;
import com.jfudali.coursesapp.exceptions.NotFoundException;
import com.jfudali.coursesapp.question.dto.AddQuestionsDto;
import com.jfudali.coursesapp.lesson.model.Lesson;
import com.jfudali.coursesapp.lesson.repository.LessonRepository;
import com.jfudali.coursesapp.question.model.Question;
import com.jfudali.coursesapp.quiz.dto.CreateQuizDto;
import com.jfudali.coursesapp.question.dto.UpdateQuestionDto;
import com.jfudali.coursesapp.quiz.dto.UpdateQuizDto;
import com.jfudali.coursesapp.quiz.model.Quiz;
import com.jfudali.coursesapp.quiz.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizService {
    private final LessonRepository lessonRepository;
    private final QuizRepository quizRepository;
    public Quiz createQuiz(Integer lessonId, CreateQuizDto createQuizDto) throws NotFoundException {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new NotFoundException("Lesson not found"));
        if(lesson.getQuiz() != null) throw new AlreadyExistsException("Lesson already has a quiz");
        Quiz quiz = Quiz.builder()
                    .title(createQuizDto.getTitle())
                    .build();
        lesson.setQuiz(quiz);
        lessonRepository.save(lesson);
        return quiz;
    }
    public Quiz updateQuiz(Integer lessonId, UpdateQuizDto updateQuizDto) throws NotFoundException {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new NotFoundException("Lesson not found"));
        Quiz quiz = lesson.getQuiz();
        if(quiz == null) throw new NotFoundException("Quiz not found");
        quiz.setTitle(updateQuizDto.getTitle());
        lesson.setQuiz(quiz);
        return quiz;
    }
    public void deleteQuiz(Integer lessonId) throws NotFoundException {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new NotFoundException("Lesson not found"));
        if(lesson.getQuiz() == null) throw  new NotFoundException("Quiz not found");
        lesson.setQuiz(null);
        lessonRepository.save(lesson);
    }

}
