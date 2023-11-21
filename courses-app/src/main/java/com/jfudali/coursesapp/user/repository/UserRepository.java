package com.jfudali.coursesapp.user.repository;

import java.util.Optional;
import java.util.Set;

import com.jfudali.coursesapp.course.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import com.jfudali.coursesapp.user.model.User;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Transactional
    Optional<User> findByEmail(String email);


}
