package com.jfudali.coursesapp.ownership.service;

import com.jfudali.coursesapp.course.model.Course;
import com.jfudali.coursesapp.course.repository.CourseRepository;
import com.jfudali.coursesapp.exceptions.AlreadyExistsException;
import com.jfudali.coursesapp.exceptions.NotFoundException;
import com.jfudali.coursesapp.exceptions.OwnershipException;
import com.jfudali.coursesapp.ownership.model.Ownership;
import com.jfudali.coursesapp.ownership.model.OwnershipKey;
import com.jfudali.coursesapp.ownership.repository.OwnershipRepository;
import com.jfudali.coursesapp.user.model.User;
import com.jfudali.coursesapp.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OwnershipService {
    private final UserService userService;
    private final CourseRepository courseRepository;
    private final OwnershipRepository ownershipRepository;
    @Transactional
    public String addCourseToOwnedCourses(String email, Integer courseId) throws NotFoundException, OwnershipException {
        User user = userService.getUserByEmail(email);
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new NotFoundException("Course not " +
                                                                                                            "found"));
        if(course.getCreator().equals(user)) throw new OwnershipException("You cannot buy this course, because you are an owner of it");
        if(user.getOwnerships().stream().anyMatch(ownership -> ownership.getCourse().equals(course))) throw  new AlreadyExistsException("You already bought this course");
        Ownership ownership = new Ownership(new OwnershipKey(courseId, user.getIduser()), course, user, false);
        ownershipRepository.save(ownership);
        return "You bought course named: " + course.getName();
    }

}
