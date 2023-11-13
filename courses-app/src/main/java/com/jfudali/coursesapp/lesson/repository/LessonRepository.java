package com.jfudali.coursesapp.lesson.repository;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.jfudali.coursesapp.lesson.model.Lesson;

public interface LessonRepository extends JpaRepository<Lesson, Integer> {
    Page<Lesson> findLessonsByCourseIdcourse(Integer courseId, Pageable pageable);
}
