package com.jfudali.coursesapp.quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCompletedQuizDto {
    private List<UserQuestionAnswerDto> userQuestionsAnswers;
}
