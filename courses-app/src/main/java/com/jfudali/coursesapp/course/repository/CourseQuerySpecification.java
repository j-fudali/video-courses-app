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

    public static Specification<Course> withCategory(String  category) {
        return (root, query, criteriaBuilder) -> {
            if (category == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true)); // True predicate to get all records
            }
            return criteriaBuilder.like(root.get("category").get("name"),
                                         category);
        };
    }
}
