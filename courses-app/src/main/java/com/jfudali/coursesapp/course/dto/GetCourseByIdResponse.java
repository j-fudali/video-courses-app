package com.jfudali.coursesapp.course.dto;

import com.jfudali.coursesapp.category.model.Category;
import com.jfudali.coursesapp.lesson.dto.PublicLesson;
import com.jfudali.coursesapp.user.dto.PublicUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetCourseByIdResponse {
    private  String name;
    private Category category;
    private PublicUser creator;
    private List<PublicLesson> lessons;
}
