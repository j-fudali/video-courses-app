package com.jfudali.coursesapp.question.dto;

import com.jfudali.coursesapp.question.model.Question;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SetQuestionsDto {
    @Size(min = 1, message = "There must be min. 1 question")
    private List<Question> questions;
}
