package com.jfudali.coursesapp.user.service;

import com.jfudali.coursesapp.course.repository.CourseRepository;
import com.jfudali.coursesapp.exceptions.NotFoundException;
import com.jfudali.coursesapp.ownership.repository.OwnershipRepository;
import com.jfudali.coursesapp.user.dto.CreatedCoursesDto;
import com.jfudali.coursesapp.user.dto.OwnedCoursesDto;
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
    private final OwnershipRepository ownershipRepository;
    private final ModelMapper modelMapper;

    public User getUserByEmail(String email) throws NotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));
    }

    public Page<?> getCurrentUserCourses(String type, String email, Pageable pageable)
            throws NotFoundException {
        if (type != null && type.equals("created")) {
            return courseRepository.findCoursesByCreatorEmail(email, pageable).map(course -> modelMapper.map(course, CreatedCoursesDto.class));
        }
        return ownershipRepository.findCoursesOwnedByUser(email, pageable);
    }
}