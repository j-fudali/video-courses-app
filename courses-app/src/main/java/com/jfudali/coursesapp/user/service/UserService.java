package com.jfudali.coursesapp.user.service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.mapping.List;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.jfudali.coursesapp.course.model.Course;
import com.jfudali.coursesapp.course.repository.CourseRepository;
import com.jfudali.coursesapp.exceptions.NotFoundException;
import com.jfudali.coursesapp.user.dto.OwnedCoursesResponse;
import com.jfudali.coursesapp.user.model.User;
import com.jfudali.coursesapp.user.repository.UserRepository;

import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final ModelMapper modelMapper;

    public Page<OwnedCoursesResponse> getCurrentUserOwnedCourses(String email, Pageable pageable)
            throws NotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));
        Page<Course> courses = courseRepository.findCoursesByUsersIduser(user.getIduser(), pageable);
        return courses.map(course -> modelMapper.map(course, OwnedCoursesResponse.class));
    }

    public String addCourseToOwnedCourses(String email, Integer courseId) throws NotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found"));
        if (!user.getCourses().contains(course)) {
            user.getCourses().add(course);
            userRepository.save(user);
            return "Course has been added to owning courses";
        }
        throw new EntityExistsException("You already has this course");
    }
}
