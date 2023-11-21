package com.jfudali.coursesapp.course.controller;

import java.security.Principal;

import com.jfudali.coursesapp.course.dto.GetCourseByIdResponse;
import com.jfudali.coursesapp.dto.ResponseMessage;
import com.jfudali.coursesapp.user.dto.BuyCourseRequest;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.jfudali.coursesapp.course.dto.CreateUpdateCourseRequest;
import com.jfudali.coursesapp.course.dto.CreateUpdateCourseResponse;
import com.jfudali.coursesapp.course.dto.GetAllCoursesResponse;
import com.jfudali.coursesapp.course.model.Course;
import com.jfudali.coursesapp.course.service.CourseService;
import com.jfudali.coursesapp.exceptions.NotFoundException;
import com.jfudali.coursesapp.exceptions.OwnershipException;

import jakarta.validation.Valid;
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
                        @RequestBody CreateUpdateCourseRequest createCourseRequest,
                        Principal principal) throws NotFoundException {
                CreateUpdateCourseResponse courseResponse = modelMapper.map(
                                courseService.createCourse(createCourseRequest, principal.getName()),
                                CreateUpdateCourseResponse.class);
                return new ResponseEntity<CreateUpdateCourseResponse>(courseResponse,
                                HttpStatus.CREATED);
        }

        @GetMapping()
        public ResponseEntity<Page<GetAllCoursesResponse>> getAllCourses(@RequestParam(required = false) String name,
                        @RequestParam(required = false) String category, Pageable pageable) {

                return ResponseEntity.ok().body(courseService.getAllCourses(name, category, pageable)
                                .map(course -> modelMapper.map(course,
                                                GetAllCoursesResponse.class)));
        }
        @GetMapping("/{id}")
        public  ResponseEntity<GetCourseByIdResponse> getCourseById(@PathVariable("id") Integer id) throws NotFoundException {
                return new ResponseEntity<>(modelMapper.map(courseService.getCourseById(id), GetCourseByIdResponse.class), HttpStatus.OK);
        }

        @PreAuthorize("hasAuthority('ADMIN')")
        @PatchMapping(value = "/{id}")
        public ResponseEntity<CreateUpdateCourseResponse> updateCourse(@PathVariable String id,
                        @RequestBody CreateUpdateCourseRequest createUpdateCourseRequest, Principal principal)
                        throws NumberFormatException, NotFoundException, OwnershipException {
                Course updatedCourse = courseService.updateCourse(Integer.valueOf(id), createUpdateCourseRequest,
                                principal.getName());
                CreateUpdateCourseResponse courseResponse = modelMapper.map(
                                updatedCourse,
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
                courseService.deleteCourse(id, principal.getName());
        }

        @PostMapping(value = "/buy")
        public ResponseEntity<ResponseMessage> buyCourse(@RequestBody BuyCourseRequest body,
                                                         Principal principal)
                throws NotFoundException, OwnershipException {
                return new ResponseEntity<>
                        (new ResponseMessage(courseService.addCourseToOwnedCourses(principal.getName(),
                                                                                   body.getCourseId())),
                         HttpStatus.OK);
        }

}
