package com.jfudali.coursesapp.course.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

import com.jfudali.coursesapp.course.model.Course;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class CourseQuerySpecification implements Specification<Course> {

    @Override
    @Nullable
    public Predicate toPredicate(Root<Course> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        // TODO
    }

}
