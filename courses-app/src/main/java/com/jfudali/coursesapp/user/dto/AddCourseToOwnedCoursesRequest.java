package com.jfudali.coursesapp.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCourseToOwnedCoursesRequest {
    private Integer courseId;
}
