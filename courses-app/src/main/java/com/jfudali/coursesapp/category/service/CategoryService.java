package com.jfudali.coursesapp.category.service;

import com.jfudali.coursesapp.category.model.Category;
import com.jfudali.coursesapp.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<Category> getCategories(){
        return this.categoryRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }
}
