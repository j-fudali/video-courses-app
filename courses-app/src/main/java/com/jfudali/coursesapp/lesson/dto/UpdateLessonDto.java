package com.jfudali.coursesapp.lesson.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateLessonDto {
    @Length(max = 100)
    private String title;
    @Length(max = 3500)
    private String description;
    @Length(max = 500)
    private String video;
}
