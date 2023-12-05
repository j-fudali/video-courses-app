package com.jfudali.coursesapp.course.dto;

import com.jfudali.coursesapp.category.model.Category;
import com.jfudali.coursesapp.lesson.dto.PublicLessonDto;
import com.jfudali.coursesapp.user.dto.PublicUserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDto {
    private  String name;
    private Category category;
    private PublicUserDto creator;
    private List<PublicLessonDto> lessons;
}
