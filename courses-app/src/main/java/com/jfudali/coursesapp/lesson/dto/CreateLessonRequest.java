package com.jfudali.coursesapp.lesson.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateLessonRequest {
    @NotBlank(message = "Title is empty")
    @NotNull(message = "Title is null")
    private String title;
    @NotBlank(message = "Description is empty")
    @NotNull(message = "Description is null")
    private String description;
    @NotBlank(message = "Video is empty")
    @NotNull(message = "Video is null")
    private String video;
}
