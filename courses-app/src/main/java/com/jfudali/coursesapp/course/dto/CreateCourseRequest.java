package com.jfudali.coursesapp.course.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CreateCourseRequest {
    private String name;
    private BigDecimal cost;
    private Integer categoryId;
}
