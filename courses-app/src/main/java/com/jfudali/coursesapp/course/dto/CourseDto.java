package com.jfudali.coursesapp.course.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jfudali.coursesapp.category.model.Category;
import com.jfudali.coursesapp.lesson.dto.PublicLessonDto;
import com.jfudali.coursesapp.user.dto.PublicUserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDto {
    private  String name;
    private BigDecimal cost;
    private String description;
    private Category category;
    private PublicUserDto creator;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<PublicLessonDto> lessons;
}
