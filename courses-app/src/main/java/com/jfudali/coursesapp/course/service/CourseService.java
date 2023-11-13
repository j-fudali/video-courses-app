package com.jfudali.coursesapp.course.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.jfudali.coursesapp.category.repository.CategoryRepository;
import com.jfudali.coursesapp.course.dto.CreateCourseRequest;
import com.jfudali.coursesapp.course.dto.CreateUpdateCourseResponse;
import com.jfudali.coursesapp.course.dto.UpdateCourseRequest;
import com.jfudali.coursesapp.course.model.Course;
import com.jfudali.coursesapp.course.repository.CourseQuerySpecification;
import com.jfudali.coursesapp.course.repository.CourseRepository;
import com.jfudali.coursesapp.exceptions.NotFoundException;
import com.jfudali.coursesapp.exceptions.OwnershipException;
import com.jfudali.coursesapp.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CourseService {
        private final CourseRepository courseRepository;
        private final CategoryRepository categoryRepository;
        private final UserRepository userRepository;

        public Course createCourse(CreateCourseRequest createCourseRequest, String user)
                        throws NotFoundException {
                Course course = Course.builder()
                                .name(createCourseRequest.getName())
                                .cost(createCourseRequest.getCost())
                                .category(categoryRepository.findById(createCourseRequest.getCategoryId())
                                                .orElseThrow(() -> new NotFoundException("Category not found")))
                                .creator(userRepository.findByEmail(user)
                                                .orElseThrow(() -> new NotFoundException("User not found")))
                                .build();
                return courseRepository.save(course);
        }

        public Course getCourseById(Integer id) throws NotFoundException {
                return courseRepository.findById(id)
                                .orElseThrow(() -> new NotFoundException("Course with provided id not found"));
        }

        public Page<Course> getAllCourses(String name, String category, Pageable pagination) {
                Specification<Course> spec = Specification.where(CourseQuerySpecification.withName(name))
                                .and(CourseQuerySpecification.withCategoryName(category));
                return courseRepository.findAll(spec, pagination);
        }

        public Course updateCourse(Integer id, UpdateCourseRequest updateCourseRequest, String user)
                        throws NotFoundException, OwnershipException {
                Course course = courseRepository.findById(id).orElseThrow(
                                () -> new NotFoundException("Course with provided id not found"));
                if (course.getCreator().getUsername().equals(user)) {
                        if (updateCourseRequest.getName() != null)
                                course.setName(updateCourseRequest.getName());
                        if (updateCourseRequest.getCategoryId() != null)
                                course.setCategory(categoryRepository.findById(updateCourseRequest.getCategoryId())
                                                .orElseThrow(() -> new NotFoundException("Category not found")));
                        courseRepository.save(course);
                        return course;
                }
                throw new OwnershipException("You are not an owner of this course");
        }

        public void deleteCourse(Integer id, String user) throws NotFoundException, OwnershipException {
                Course course = this.getCourseById(id);
                if (course.getCreator().getUsername().equals(user)) {
                        courseRepository.deleteById(id);
                        return;
                }
                throw new OwnershipException("You are not an owner of this course");
        }

}
