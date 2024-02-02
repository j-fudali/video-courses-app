package com.jfudali.coursesapp.quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateQuizDto {
    @Length(max = 100, message = "Title max length is 100 characters")
    private String title;
}
