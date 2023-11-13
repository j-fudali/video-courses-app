package com.jfudali.coursesapp.user.dto;

import com.jfudali.coursesapp.category.model.Category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OwnedCoursesResponse {
    private Integer idcourse;
    private String name;
    private Category category;
}
