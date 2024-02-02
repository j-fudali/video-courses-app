package com.jfudali.coursesapp.course.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CreateCourseDto {
    @NotBlank(message = "Name should not be empty")
    @NotNull(message = "Name should not be null")
    @Length(message = "Name maximum length is 45 characters", max = 45)
    private String name;
    @NotBlank(message = "Description should not be empty")
    @NotNull(message = "Description should not be null")
    @Length(min = 1, max = 500, message = "Description must be between 1 and 500 characters long")
    private String description;
    @NotNull(message = "Cost should not be null")
    private BigDecimal cost;
    @NotNull(message = "CategoryId should not be null")
    private Integer categoryId;
}
