package com.jfudali.coursesapp.course.dto;

import java.math.BigDecimal;

import com.jfudali.coursesapp.category.model.Category;
import com.jfudali.coursesapp.user.model.PublicUser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllCoursesResponse {
    private Integer idcourse;
    private String name;
    private BigDecimal cost;
    private Category category;
    private PublicUser creator;
}
