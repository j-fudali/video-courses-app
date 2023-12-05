package com.jfudali.coursesapp.user.dto;

import com.jfudali.coursesapp.category.model.Category;
import lombok.*;

@Getter
@Setter
public class OwnedCoursesDto {
    private Integer idcourse;
    private String name;
    private Category category;
    private boolean isCompleted;

    public OwnedCoursesDto(Integer idcourse, String name, Category category, boolean isCompleted) {
        this.idcourse = idcourse;
        this.name = name;
        this.category = category;
        this.isCompleted = isCompleted;
    }
}
