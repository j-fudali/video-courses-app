package com.jfudali.coursesapp.course.service;

import com.jfudali.coursesapp.course.dto.GetAllCoursesDto;
import com.jfudali.coursesapp.course.dto.UpdateCourseDto;
import com.jfudali.coursesapp.dto.ResponseMessage;
import com.jfudali.coursesapp.ownership.model.Ownership;
import com.jfudali.coursesapp.ownership.model.OwnershipKey;
import com.jfudali.coursesapp.ownership.repository.OwnershipRepository;
import com.jfudali.coursesapp.user.model.User;
import com.jfudali.coursesapp.user.service.UserService;
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
        private final UserService userService;
        private final OwnershipRepository ownershipRepository;
        @Transactional
        public Course createCourse(CreateCourseDto createCourseRequest, String user)
                        throws NotFoundException {
                Course course = Course.builder()
                                .name(createCourseRequest.getName())
                                .cost(createCourseRequest.getCost())
                                .description(createCourseRequest.getDescription())
                                .category(categoryRepository.findById(createCourseRequest.getCategoryId())
                                                .orElseThrow(() -> new NotFoundException("Category not found")))
                                .creator(userRepository.findByEmail(user)
                                                .orElseThrow(() -> new NotFoundException("User not found")))
                                .build();
                return courseRepository.save(course);
        }

        public Course getCourseById(Integer id) throws NotFoundException {
            return courseRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("Course not found"));
        }

        public Page<GetAllCoursesDto> getAllCourses(String name, Integer categoryId, String userEmail, Pageable pagination) {
        return  courseRepository.findAll(userEmail, name, categoryId, pagination);
        }
        public Page<Course> getAllCourses(String name, Integer categoryId, Pageable pagination) {
                Specification<Course> spec = Specification.where(CourseQuerySpecification.withName(name))
                                .and(CourseQuerySpecification.withCategoryId(categoryId));
                return  courseRepository.findAll(spec, pagination);
        }
        @Transactional
        public Course updateCourse(Integer id, UpdateCourseDto updateCourseDto, String user)
                        throws NotFoundException, OwnershipException {
                Course course = courseRepository.findById(id).orElseThrow(
                                () -> new NotFoundException("Course with provided id not found"));
                if (updateCourseDto.getName() != null)
                        course.setName(updateCourseDto.getName());
                if(updateCourseDto.getCost() != null){
                        course.setCost(updateCourseDto.getCost());
                }
                if(updateCourseDto.getDescription() != null){
                        course.setDescription(updateCourseDto.getDescription());
                }
                if (updateCourseDto.getCategoryId() != null)
                        course.setCategory(categoryRepository.findById(updateCourseDto.getCategoryId())
                                        .orElseThrow(() -> new NotFoundException("Category not found")));
                courseRepository.save(course);
                return course;
        }

        public ResponseMessage deleteCourse(Integer id, String userEmail) throws NotFoundException, OwnershipException {
                courseRepository.deleteById(id);
                return new ResponseMessage("Course has been deleted");
        }

        @Transactional
        public void verifyUserPassedCourse(Integer courseId, String userEmail){
                Course course = this.getCourseById(courseId);
                User user = this.userService.getUserByEmail(userEmail);
                if(course.getLessons().size() == user.getPassedLessons().stream().filter(lesson -> lesson.getCourse().equals(course)).toList().size()){
                        Ownership ownership =
                                this.ownershipRepository.findById(new OwnershipKey(course.getIdcourse(), user.getIduser())).orElseThrow(() -> new NotFoundException("Ownerhsip not found"));
                        ownership.setIsCompleted(true);
                        System.out.println(ownership);
                        ownershipRepository.save(ownership);
                }
        }

}
