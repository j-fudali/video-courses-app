package com.jfudali.coursesapp.question.dto;

import com.jfudali.coursesapp.question.model.Question;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateQuestionDto {
    @NotEmpty(message = "Questions cannot be empty")
    private List<Question> questions;
}
