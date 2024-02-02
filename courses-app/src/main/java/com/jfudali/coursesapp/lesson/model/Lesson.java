package com.jfudali.coursesapp.lesson.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jfudali.coursesapp.course.model.Course;

import com.jfudali.coursesapp.quiz.model.Quiz;
import com.jfudali.coursesapp.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

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
    @ManyToMany(mappedBy = "passedLessons")
    Set<User> usersPassedLesson;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "course_idcourse")
    private Course course;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "quiz_idquiz")
    private Quiz quiz;
}
