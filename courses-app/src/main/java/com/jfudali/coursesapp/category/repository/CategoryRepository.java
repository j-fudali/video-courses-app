package com.jfudali.coursesapp.category.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jfudali.coursesapp.category.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findByIdcategory(Integer idcategory);
}
