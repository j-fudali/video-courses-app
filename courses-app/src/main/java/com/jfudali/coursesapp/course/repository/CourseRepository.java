package com.jfudali.coursesapp.course.repository;

import com.jfudali.coursesapp.course.dto.GetAllCoursesDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.jfudali.coursesapp.course.model.Course;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CourseRepository
        extends JpaRepository<Course, Integer>,
        JpaSpecificationExecutor<Course> {

    @Query("select new com.jfudali.coursesapp.course.dto.GetAllCoursesDto(c.idcourse, c.name, c.description, c.cost," +
            " c.category, c.creator,  (COUNT(o) > 0)) from Course c left join Ownership o on (o.course.idcourse = c" +
            ".idcourse and o.user.email = :userEmail) where (:name is null or" +
            " c.name like %:name%) and " +
            "(:category is null or c.category.name like :category) " +
            "group" +
            " by c")
    Page<GetAllCoursesDto> findAll(@Param("userEmail") String userEmail, @Param("name") String name, @Param(
            "category") String category, Pageable pageable);

    Page<Course> findCoursesByCreatorEmail(String email, Pageable pageable);
}
