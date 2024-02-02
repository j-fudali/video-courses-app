package com.jfudali.coursesapp.lesson.repository;

import com.jfudali.coursesapp.lesson.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LessonRepository extends JpaRepository<Lesson, Integer> {
}
