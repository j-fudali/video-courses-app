package com.jfudali.coursesapp.category.controller;

import com.jfudali.coursesapp.category.model.Category;
import com.jfudali.coursesapp.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;
    @GetMapping
    public ResponseEntity<List<Category>> getCategories(){
        return new ResponseEntity<>(this.categoryService.getCategories(), HttpStatus.OK);
    }
}
