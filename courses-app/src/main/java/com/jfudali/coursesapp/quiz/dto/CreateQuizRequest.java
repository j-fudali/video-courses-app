package com.jfudali.coursesapp.quiz.dto;

import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateQuizRequest {
    @Length(max = 100, message = "Title max length is 100 characters")
    private String title;
}
