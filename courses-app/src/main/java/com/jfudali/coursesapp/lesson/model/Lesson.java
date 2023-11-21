package com.jfudali.coursesapp.lesson.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jfudali.coursesapp.course.model.Course;

import com.jfudali.coursesapp.quiz.model.Quiz;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "lesson")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
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
    @OneToMany(mappedBy = "lesson")
    private  List<Quiz> quizzes;
}
