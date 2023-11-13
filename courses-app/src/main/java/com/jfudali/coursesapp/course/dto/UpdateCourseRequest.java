package com.jfudali.coursesapp.course.dto;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCourseRequest {
    @Nullable
    private String name;
    @Nullable
    private Integer categoryId;
}
