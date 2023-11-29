package com.jfudali.coursesapp.question.dto;

import com.jfudali.coursesapp.question.model.Question;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddQuestionsDto {
    @NotEmpty(message = "Questions list cannot be empty")
    private  List<Question> questions;
}
