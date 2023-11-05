package com.jfudali.coursesapp.course.controller;

import java.security.Principal;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.jfudali.coursesapp.course.dto.CreateCourseRequest;
import com.jfudali.coursesapp.course.dto.CreateUpdateCourseResponse;
import com.jfudali.coursesapp.course.dto.GetAllCoursesResponse;
import com.jfudali.coursesapp.course.dto.UpdateCourseRequest;
import com.jfudali.coursesapp.course.model.Course;
import com.jfudali.coursesapp.course.service.CourseService;
import com.jfudali.coursesapp.exceptions.NotFoundException;
import com.jfudali.coursesapp.exceptions.OwnershipException;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/courses")
@RequiredArgsConstructor
@Validated
public class CourseController {
    private final CourseService courseService;
    private final ModelMapper modelMapper;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping()
    public ResponseEntity<CreateUpdateCourseResponse> createCourse(
            @RequestBody @Valid CreateCourseRequest createCourseRequest,
            Authentication authentication) throws NotFoundException {
        CreateUpdateCourseResponse courseResponse = modelMapper.map(
                courseService.createCourse(createCourseRequest, authentication.getName()),
                CreateUpdateCourseResponse.class);
        return new ResponseEntity<CreateUpdateCourseResponse>(courseResponse,
                HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<Page<GetAllCoursesResponse>> getAllCourses(@RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @Min(value = 1, message = "Page number must be equal or higher than 1") @RequestParam(required = false) Integer page,
            @Min(value = 1, message = "Size number must be equal or higher than 1") @RequestParam(required = false) Integer size) {
        Pageable pagination = PageRequest.of(page != null ? page - 1 : 0, size != null ? size : 10);
        return ResponseEntity.ok().body(courseService.getAllCourses(name, category, pagination)
                .map(course -> {
                    GetAllCoursesResponse coursesResponse = modelMapper.map(course, GetAllCoursesResponse.class);
                    return coursesResponse;
                }));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping(value = "/{id}")
    public ResponseEntity<CreateUpdateCourseResponse> updateCourse(@PathVariable String id,
            @RequestBody UpdateCourseRequest updateCourseRequest, Principal principal)
            throws NumberFormatException, NotFoundException, OwnershipException {
        CreateUpdateCourseResponse courseResponse = modelMapper.map(
                courseService.updateCourse(Integer.valueOf(id), updateCourseRequest, principal.getName()),
                CreateUpdateCourseResponse.class);
        return new ResponseEntity<CreateUpdateCourseResponse>(
                courseResponse,
                HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteCourse(@PathVariable @NumberFormat Integer id, Principal principal)
            throws NumberFormatException, NotFoundException, OwnershipException {
        courseService.deleteCourse(Integer.valueOf(id), principal.getName());
    }

}
