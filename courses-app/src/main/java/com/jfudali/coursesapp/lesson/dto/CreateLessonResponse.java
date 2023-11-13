package com.jfudali.coursesapp.lesson.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateLessonResponse {
    private String title;
    private String description;
    private String video;
}
