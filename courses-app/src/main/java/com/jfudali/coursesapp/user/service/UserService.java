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
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final OwnershipRepository ownershipRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public String changePassword(String oldPassword, String newPassword,
                                 String userEmail){
        User user = this.getUserByEmail(userEmail);
        if(passwordEncoder.matches(oldPassword, user.getPassword())){
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return "Password has been changed";
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Old " +
                "password is incorrect");
    }

    public User getUserByEmail(String email) throws NotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));
    }
    @Transactional
    public String updateUser(String firstname, String lastname,
                            String userEmail){
        User user = this.getUserByEmail(userEmail);
        if(firstname != null){
            user.setFirstname(firstname);
        }
        if(lastname != null){
            user.setLastname(lastname);
        }
        userRepository.save(user);
        return "User has been updated";
    }
    @Transactional(readOnly = true)
    public Page<?> getCurrentUserCourses(String type, String email, Pageable pageable)
            throws NotFoundException {
        if (type != null && type.equals("created")) {
            return courseRepository.findCoursesByCreatorEmail(email, pageable).map(course -> modelMapper.map(course, CreatedCoursesDto.class));
        }
        return ownershipRepository.findCoursesOwnedByUser(email, pageable);
    }
    
}