package com.jfudali.coursesapp.lesson.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.jfudali.coursesapp.course.model.Course;

import com.jfudali.coursesapp.quiz.model.Quiz;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "lesson")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idlesson;
    private String title;
    private String description;
    private String video;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "course_idcourse")
    private Course course;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "quiz_idquiz")
    private Quiz quiz;
}
