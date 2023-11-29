package com.jfudali.coursesapp.course.service;

import com.jfudali.coursesapp.course.dto.UpdateCourseDto;
import com.jfudali.coursesapp.dto.ResponseMessage;
import com.jfudali.coursesapp.exceptions.AlreadyExistsException;
import com.jfudali.coursesapp.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.jfudali.coursesapp.category.repository.CategoryRepository;
import com.jfudali.coursesapp.course.dto.CreateCourseDto;
import com.jfudali.coursesapp.course.model.Course;
import com.jfudali.coursesapp.course.repository.CourseQuerySpecification;
import com.jfudali.coursesapp.course.repository.CourseRepository;
import com.jfudali.coursesapp.exceptions.NotFoundException;
import com.jfudali.coursesapp.exceptions.OwnershipException;
import com.jfudali.coursesapp.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CourseService {
        private final CourseRepository courseRepository;
        private final CategoryRepository categoryRepository;
        private final UserRepository userRepository;
        public Course createCourse(CreateCourseDto createCourseRequest, String user)
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

        @Transactional
        public Course getCourseById(Integer id) throws NotFoundException {
                return courseRepository.findById(id)
                                .orElseThrow(() -> new NotFoundException("Course with provided id not found"));
        }

        public Page<Course> getAllCourses(String name, String category, Pageable pagination) {
                Specification<Course> spec = Specification.where(CourseQuerySpecification.withName(name))
                                .and(CourseQuerySpecification.withCategoryName(category));
                return courseRepository.findAll(spec, pagination);
        }

        public Course updateCourse(Integer id, UpdateCourseDto updateCourseDto, String user)
                        throws NotFoundException, OwnershipException {
                Course course = courseRepository.findById(id).orElseThrow(
                                () -> new NotFoundException("Course with provided id not found"));
                if (updateCourseDto.getName() != null)
                        course.setName(updateCourseDto.getName());
                if (updateCourseDto.getCategoryId() != null)
                        course.setCategory(categoryRepository.findById(updateCourseDto.getCategoryId())
                                        .orElseThrow(() -> new NotFoundException("Category not found")));
                courseRepository.save(course);
                return course;
        }

        @Transactional
        public ResponseMessage deleteCourse(Integer id, String userEmail) throws NotFoundException, OwnershipException {
                Course course = this.getCourseById(id);
                for (User user :
                        course.getUsers()) {
                        user.getCourses().remove(course);
                }
                course.getUsers().clear();
                courseRepository.deleteById(id);
                return new ResponseMessage("Course has been deleted");
        }
        @Transactional
        public String addCourseToOwnedCourses(String email, Integer courseId) throws NotFoundException, OwnershipException {
                User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));
                Course course = courseRepository.findById(courseId).orElseThrow(() -> new NotFoundException("Course not found"));
                if(course.getCreator().equals(user)) throw new OwnershipException("You cannot buy this course, because you are an owner of it");
                if(user.getCourses().contains(course)) throw  new AlreadyExistsException("You already bought this course");
                user.getCourses().add(course);
                course.getUsers().add(user);
                userRepository.save(user);
                courseRepository.save(course);
                return "You bought course named: " + course.getName();


        }
}
