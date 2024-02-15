package com.jfudali.coursesapp.course.controller;

import com.jfudali.coursesapp.course.dto.CreateCourseDto;
import com.jfudali.coursesapp.course.dto.GetAllCoursesDto;
import com.jfudali.coursesapp.course.dto.GetCourseById;
import com.jfudali.coursesapp.course.dto.UpdateCourseDto;
import com.jfudali.coursesapp.course.model.Course;
import com.jfudali.coursesapp.course.service.CourseService;
import com.jfudali.coursesapp.exceptions.NotFoundException;
import com.jfudali.coursesapp.exceptions.OwnershipException;
import com.jfudali.coursesapp.lesson.dto.PublicLessonDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/courses")
@RequiredArgsConstructor
public class CourseController {
        private final CourseService courseService;
        private final ModelMapper modelMapper;
        @PreAuthorize("hasAuthority('ADMIN')")
        @PostMapping()
        public ResponseEntity<Map<String, Integer>> createCourse(
                @Valid @RequestBody CreateCourseDto createCourseRequest,
                        Principal principal) throws NotFoundException {
                Integer courseId = courseService.createCourse(createCourseRequest,principal.getName()).getIdcourse();
                Map<String, Integer> response = new HashMap<>();
                response.put("courseId", courseId);
                return new ResponseEntity<>(response,
                                            HttpStatus.CREATED);
        }

        @GetMapping()
        public ResponseEntity<Page<GetAllCoursesDto>> getAllCourses(@RequestParam(required = false) String name,
                                                                    @RequestParam(required = false) String category, Pageable pageable, Principal principal) {
                if(principal != null){
                        return new ResponseEntity<>(courseService.getAllCourses(name, category, principal.getName(), pageable),HttpStatus.OK);
                }
                return  new ResponseEntity<>(courseService.getAllCourses(name, category, pageable).map(course ->
                         modelMapper.map(course, GetAllCoursesDto.class)
                ), HttpStatus.OK);
        }
        @GetMapping("/{id}")
        public  ResponseEntity<?> getCourseById(@PathVariable("id") Integer id, Principal principal) throws NotFoundException {
                Course course = courseService.getCourseById(id);
                GetCourseById result =
                        modelMapper.map(course, GetCourseById.class);
                result.setIsBought(false);
                if(principal != null && course.getOwnerships().stream().anyMatch(ownership -> ownership.getUser().getEmail().equals(principal.getName()))){
                       List<PublicLessonDto> lessonDtos =
                               course.getLessons().stream().map(lesson -> new PublicLessonDto(
                                       lesson.getIdlesson(),
                                       lesson.getTitle(),
                                       lesson.getDescription(),
                                       lesson.getUsersPassedLesson().stream().anyMatch(user -> user.getEmail().equals(principal.getName())))).toList();
                       result.setLessons(lessonDtos);
                       result.setIsBought(true);
                }
                if(principal==null){
                        result.setIsBought(null);
                }
                return new ResponseEntity<>(result,
                                            HttpStatus.OK);
        }

        @PatchMapping(value = "/{id}")
        public ResponseEntity<GetCourseById> updateCourse(@PathVariable String id,
                                                          @Valid @RequestBody UpdateCourseDto updateCourseDto, Principal principal)
                        throws NumberFormatException, NotFoundException, OwnershipException {
                Course updatedCourse = courseService.updateCourse(Integer.valueOf(id), updateCourseDto,
                                                                  principal.getName());
                GetCourseById courseResponse = modelMapper.map(
                                updatedCourse,
                                GetCourseById.class);
                return new ResponseEntity<>(
                                courseResponse,
                                HttpStatus.OK);
        }

}
