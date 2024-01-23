package com.jfudali.coursesapp.course.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

@Getter
@Setter
public class UpdateCourseDto {
    @Length(message = "Name maximum length is 45 characters", max = 45)
    private String name;
    @Length(min = 1, max = 500, message = "Description must be between 1 and 500 characters long")
    private String description;
    @Min(value = (long)0.01, message = "Minimum value is 0.01")
    private BigDecimal cost;
    private Integer categoryId;
}
