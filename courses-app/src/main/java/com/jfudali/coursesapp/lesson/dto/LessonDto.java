package com.jfudali.coursesapp.lesson.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jfudali.coursesapp.quiz.dto.QuizDto;
import com.jfudali.coursesapp.quiz.model.Quiz;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LessonDto {
    private  String title;
    private  String description;
    private  String video;
    private QuizDto quiz;

}
