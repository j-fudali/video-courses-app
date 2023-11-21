package com.jfudali.coursesapp.course.repository;

import com.jfudali.coursesapp.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.jfudali.coursesapp.course.model.Course;

import java.util.Optional;
import java.util.Set;

public interface CourseRepository
        extends JpaRepository<Course, Integer>,
        JpaSpecificationExecutor<Course> {
    Page<Course> findCoursesByUsersIduser(Integer userId, Pageable pageable);
}
