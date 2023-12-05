package com.jfudali.coursesapp.ownership.controller;

import com.jfudali.coursesapp.dto.ResponseMessage;
import com.jfudali.coursesapp.exceptions.NotFoundException;
import com.jfudali.coursesapp.exceptions.OwnershipException;
import com.jfudali.coursesapp.ownership.service.OwnershipService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/courses/{courseId}/buy")
public class OwnershipController {
    private final OwnershipService ownershipService;

    @PostMapping()
    public ResponseEntity<ResponseMessage> buyCourse(@PathVariable("courseId") @Valid
                                                     @NotBlank(message = "Course id cannot be blank")
                                                     @NotNull(message = "Course id cannot be null")
                                                     Integer id,
                                                     Principal principal)
            throws NotFoundException, OwnershipException {
        return new ResponseEntity<>
                (new ResponseMessage(ownershipService.addCourseToOwnedCourses(principal.getName(),
                                                                           id)),
                 HttpStatus.OK);
    }
}
