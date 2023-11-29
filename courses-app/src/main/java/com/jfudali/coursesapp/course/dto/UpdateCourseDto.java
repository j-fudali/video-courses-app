package com.jfudali.coursesapp.course.dto;

import jakarta.validation.constraints.Max;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UpdateCourseDto {
    private String name;
    @Max(message = "Name maximum length is 45 characters", value = 45)
    private BigDecimal cost;
    private Integer categoryId;
}
