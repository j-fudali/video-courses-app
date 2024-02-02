package com.jfudali.coursesapp.course.repository;

import org.springframework.data.jpa.domain.Specification;
import com.jfudali.coursesapp.course.model.Course;

public class CourseQuerySpecification {

    public static Specification<Course> withName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isEmpty()) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true)); // True predicate to get all records
            }
            return criteriaBuilder.like(root.get("name"), "%" + name + "%");
        };
    }

    public static Specification<Course> withCategoryId(Integer categoryId) {
        return (root, query, criteriaBuilder) -> {
            if (categoryId == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true)); // True predicate to get all records
            }
            return criteriaBuilder.equal(root.get("category").get("idcategory"), categoryId);
        };
    }
}
