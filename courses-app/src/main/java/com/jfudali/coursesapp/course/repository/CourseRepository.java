package com.jfudali.coursesapp.course.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jfudali.coursesapp.course.model.Course;
import com.jfudali.coursesapp.course.model.ECourseOwnership;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    // @Query(value = "SELECT name, cost, category_idcategory FROM course WHERE" +
    // "(:name IS NULL OR name = :name)"
    // + "AND (:type IS NULL OR (:type = ''))")
    // List<Course> searchCourses(@Param("name") String name, @Param("type")
    // ECourseOwnership type);
    // List<Course> findByName(String name);
}
