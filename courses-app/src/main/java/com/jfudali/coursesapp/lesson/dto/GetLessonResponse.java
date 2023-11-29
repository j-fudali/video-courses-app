package com.jfudali.coursesapp.lesson.dto;

import com.jfudali.coursesapp.quiz.model.Quiz;
import jakarta.persistence.OneToMany;

import java.util.List;

public class GetLessonResponse {
    private  String title;
    private  String description;
    private  String video;
    private Quiz quiz;

}
