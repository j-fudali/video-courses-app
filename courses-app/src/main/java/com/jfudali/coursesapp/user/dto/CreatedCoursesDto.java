package com.jfudali.coursesapp.user.dto;

import com.jfudali.coursesapp.category.model.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreatedCoursesDto {
    private Integer idcourse;
    private String name;
    private BigDecimal cost;
    private Category category;
}
