package com.jfudali.coursesapp.question.model;

import com.jfudali.coursesapp.quiz.model.Quiz;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "question")
@AllArgsConstructor
@NoArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idquestion;
    private String text;
    @ManyToOne
    @JoinColumn(name = "quiz_idquiz")
    private Quiz quiz;
}
