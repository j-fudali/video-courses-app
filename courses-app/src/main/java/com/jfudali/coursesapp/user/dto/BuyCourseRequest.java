package com.jfudali.coursesapp.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuyCourseRequest {
    @NotBlank(message = "Course id cannot be empty")
    @NotNull(message = "Course id cannot be null")
    private Integer courseId;
}
