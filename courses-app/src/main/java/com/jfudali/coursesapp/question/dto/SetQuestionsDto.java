package com.jfudali.coursesapp.question.dto;

import com.jfudali.coursesapp.question.model.Question;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SetQuestionsDto {
    private List<Question> questions;
}
