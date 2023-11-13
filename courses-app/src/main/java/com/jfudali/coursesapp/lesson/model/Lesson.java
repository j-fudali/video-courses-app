package com.jfudali.coursesapp.lesson.model;

import com.jfudali.coursesapp.course.model.Course;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @ManyToOne
    @JoinColumn(name = "course_idcourse")
    private Course course;
}
