package com.jfudali.coursesapp.user.service;

import com.jfudali.coursesapp.course.model.Course;
import com.jfudali.coursesapp.course.repository.CourseRepository;
import com.jfudali.coursesapp.exceptions.NotFoundException;
import com.jfudali.coursesapp.user.dto.OwnedCoursesResponse;
import com.jfudali.coursesapp.user.model.User;
import com.jfudali.coursesapp.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    public User getUserByEmail(String email) throws NotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));
    }

    public Page<Course> getCurrentUserOwnedCourses(String email, Pageable pageable)
            throws NotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));
        Page<Course> courses = courseRepository.findCoursesByUsersIduser(user.getIduser(), pageable);
        return courses;
    }

}
