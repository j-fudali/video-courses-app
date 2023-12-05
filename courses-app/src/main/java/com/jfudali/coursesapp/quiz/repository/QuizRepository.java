package com.jfudali.coursesapp.quiz.repository;

import com.jfudali.coursesapp.quiz.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QuizRepository extends JpaRepository<Quiz, Integer> {
}
