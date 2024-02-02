package com.jfudali.coursesapp.user.service;

import com.jfudali.coursesapp.course.repository.CourseRepository;
import com.jfudali.coursesapp.dto.ResponseMessage;
import com.jfudali.coursesapp.exceptions.NotFoundException;
import com.jfudali.coursesapp.ownership.repository.OwnershipRepository;
import com.jfudali.coursesapp.user.dto.CreatedCoursesDto;
import com.jfudali.coursesapp.user.model.User;
import com.jfudali.coursesapp.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(readOnly = true)
    public Page<?> getCurrentUserCourses(String type, String email, Pageable pageable)
            throws NotFoundException {
        if (type != null && type.equals("created")) {
            return courseRepository.findCoursesByCreatorEmail(email, pageable).map(course -> modelMapper.map(course, CreatedCoursesDto.class));
        }
        return ownershipRepository.findCoursesOwnedByUser(email, pageable);
    }
    @Transactional(readOnly = true)
    public ResponseMessage checkHasBought(Integer courseId, String userEmail){
        boolean alreadyBought = ownershipRepository.findByCourseIdcourseAndUserEmail(courseId, userEmail).isPresent();
        if(alreadyBought){
            return new ResponseMessage("User already bought course");
        }
        throw new NotFoundException("Ownership not found");
    }
}