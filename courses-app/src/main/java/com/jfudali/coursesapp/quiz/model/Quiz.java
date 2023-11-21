package com.jfudali.coursesapp.quiz.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jfudali.coursesapp.lesson.model.Lesson;
import com.jfudali.coursesapp.question.model.Question;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "quiz")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Integer idquiz;
    private  String title;
    @ManyToOne
    @JoinColumn(name = "lesson_idlesson")
    @JsonIgnore
    private Lesson lesson;
    @OneToMany(mappedBy = "quiz")
    private List<Question> questions;
}
