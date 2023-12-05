package com.jfudali.coursesapp.course.controller;

import java.security.Principal;

import com.jfudali.coursesapp.course.dto.*;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.jfudali.coursesapp.course.model.Course;
import com.jfudali.coursesapp.course.service.CourseService;
import com.jfudali.coursesapp.exceptions.NotFoundException;
import com.jfudali.coursesapp.exceptions.OwnershipException;

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
        public ResponseEntity<CreateCourseDto> createCourse(
                        @RequestBody CreateCourseDto createCourseRequest,
                        Principal principal) throws NotFoundException {
                CreateCourseDto courseResponse = modelMapper.map(
                                courseService.createCourse(createCourseRequest, principal.getName()),
                                CreateCourseDto.class);
                return new ResponseEntity<CreateCourseDto>(courseResponse,
                                HttpStatus.CREATED);
        }

        @GetMapping()
        public ResponseEntity<Page<GetAllCoursesDto>> getAllCourses(@RequestParam(required = false) String name,
                                                                    @RequestParam(required = false) String category, Pageable pageable) {

                return ResponseEntity.ok().body(courseService.getAllCourses(name, category, pageable)
                                .map(course -> modelMapper.map(course,
                                                GetAllCoursesDto.class)));
        }
        @GetMapping("/{id}")
        public  ResponseEntity<CourseDto> getCourseById(@PathVariable("id") Integer id) throws NotFoundException {
                return new ResponseEntity<>(modelMapper.map(courseService.getCourseById(id), CourseDto.class), HttpStatus.OK);
        }

        @PatchMapping(value = "/{id}")
        public ResponseEntity<CourseDto> updateCourse(@PathVariable String id,
                                                                       @RequestBody UpdateCourseDto updateCourseDto, Principal principal)
                        throws NumberFormatException, NotFoundException, OwnershipException {
                Course updatedCourse = courseService.updateCourse(Integer.valueOf(id), updateCourseDto,
                                                                  principal.getName());
                CourseDto courseResponse = modelMapper.map(
                                updatedCourse,
                                CourseDto.class);
                return new ResponseEntity<>(
                                courseResponse,
                                HttpStatus.OK);
        }
        //TODO
        // Consider this ability for courses creators, think about ability to close for sale f.e
//        @PreAuthorize("hasAuthority('ADMIN')")
//        @DeleteMapping(value = "/{id}")
//        public ResponseEntity<ResponseMessage> deleteCourse(@PathVariable @NumberFormat Integer id, Principal principal)
//                        throws NumberFormatException, NotFoundException, OwnershipException {
//                return new ResponseEntity<>(courseService.deleteCourse(id, principal.getName()), HttpStatus.OK);
//        }



}
