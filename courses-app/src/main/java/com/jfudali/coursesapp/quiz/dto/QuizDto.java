package com.jfudali.coursesapp.quiz.dto;

import com.jfudali.coursesapp.answer.model.Answer;
import com.jfudali.coursesapp.question.model.Question;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizDto {
    private String title;
    private List<Question> questions;
    private Boolean isPassed;
}
