package com.jfudali.coursesapp.course.dto;

import java.math.BigDecimal;

import com.jfudali.coursesapp.category.model.Category;
import com.jfudali.coursesapp.user.dto.PublicUserDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllCoursesDto {
    private Integer idcourse;
    private String name;
    private String description;
    private BigDecimal cost;
    private Category category;
    private PublicUserDto creator;
}
