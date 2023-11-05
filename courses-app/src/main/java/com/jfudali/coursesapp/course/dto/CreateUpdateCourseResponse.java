package com.jfudali.coursesapp.course.dto;

import java.math.BigDecimal;

import com.jfudali.coursesapp.category.model.Category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUpdateCourseResponse {
    private String name;
    private BigDecimal cost;
    private Category category;
}
