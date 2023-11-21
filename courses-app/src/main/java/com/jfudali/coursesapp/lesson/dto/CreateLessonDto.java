package com.jfudali.coursesapp.lesson.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateLessonDto {
    @NotBlank(message = "Title is empty")
    @NotNull(message = "Title is null")
    @Length(max = 100)
    private String title;
    @NotBlank(message = "Description is empty")
    @NotNull(message = "Description is null")
    @Length(max = 350)
    private String description;
    @NotBlank(message = "Video is empty")
    @NotNull(message = "Video is null")
    @Length(max = 500)
    private String video;
}
