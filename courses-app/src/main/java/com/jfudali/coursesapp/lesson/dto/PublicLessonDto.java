package com.jfudali.coursesapp.lesson.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class PublicLessonDto {
    private Integer idlesson;
    private  String title;
    private  String description;
    private Boolean isCompleted = null;
}
