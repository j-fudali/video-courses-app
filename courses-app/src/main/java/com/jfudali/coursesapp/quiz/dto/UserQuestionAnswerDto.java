package com.jfudali.coursesapp.quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserQuestionAnswerDto {
    private Integer questionId;
    private Set<Integer> selectedAnswers;
}
