package com.jfudali.coursesapp.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jfudali.coursesapp.user.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
}
