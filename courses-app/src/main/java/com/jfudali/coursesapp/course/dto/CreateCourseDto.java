package com.jfudali.coursesapp.course.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CreateCourseDto {
    @NotBlank(message = "Name should not be empty")
    @NotNull(message = "Name should not be null")
    private String name;
    @NotBlank(message = "Cost should not be empty")
    @NotNull(message = "Cost should not be null")
    @Max(message = "Name maximum length is 45 characters", value = 45)
    private BigDecimal cost;
    @NotBlank(message = "CategoryId should not be empty")
    @NotNull(message = "CategoryId should not be null")
    private Integer categoryId;
}
