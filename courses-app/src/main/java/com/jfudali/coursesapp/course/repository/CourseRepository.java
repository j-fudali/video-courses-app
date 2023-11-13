package com.jfudali.coursesapp.course.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.jfudali.coursesapp.course.model.Course;

public interface CourseRepository
        extends JpaRepository<Course, Integer>,
        JpaSpecificationExecutor<Course> {
    Page<Course> findCoursesByUsersIduser(Integer userId, Pageable pageable);
}
