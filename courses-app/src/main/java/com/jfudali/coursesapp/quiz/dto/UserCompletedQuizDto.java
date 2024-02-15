package com.jfudali.coursesapp.quiz.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCompletedQuizDto {
    @Size(min = 1, message = "There must be at least 1 answer")
    private List<UserQuestionAnswerDto> userQuestionsAnswers;
}
