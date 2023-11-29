package com.jfudali.coursesapp.question.service;

import com.jfudali.coursesapp.exceptions.NotFoundException;
import com.jfudali.coursesapp.lesson.model.Lesson;
import com.jfudali.coursesapp.lesson.repository.LessonRepository;
import com.jfudali.coursesapp.lesson.service.LessonService;
import com.jfudali.coursesapp.question.dto.AddQuestionsDto;
import com.jfudali.coursesapp.question.dto.UpdateQuestionDto;
import com.jfudali.coursesapp.question.model.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final LessonRepository lessonRepository;
    public String addQuestions(Integer lessonId, AddQuestionsDto addQuestionsDto) throws NotFoundException {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new NotFoundException("Lesson not found"));
        if(lesson.getQuiz() == null) throw  new NotFoundException("Quiz not found");
        List<Question> questions = addQuestionsDto.getQuestions()
                .stream()
                .peek(question -> {
                    question.setQuiz(lesson.getQuiz());
                    question.setAnswers(
                            question.getAnswers()
                                    .stream()
                                    .peek(answer -> answer.setQuestion(question)).collect(Collectors.toList()));
                }).toList();
        lesson.getQuiz().getQuestions().addAll(questions);
        lessonRepository.save(lesson);
        return "Questions added successfully";
    }
    public String updateQuestion(Integer lessonId, UpdateQuestionDto updateQuestionDto){
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new NotFoundException("Lesson not found"));
        if(lesson.getQuiz() == null) throw  new NotFoundException("Quiz not found");
        lesson.getQuiz().setQuestions(updateQuestionDto.getQuestions());
        lessonRepository.save(lesson);
        return "Questions has been updated";
    }
}
