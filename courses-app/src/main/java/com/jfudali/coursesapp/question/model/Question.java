package com.jfudali.coursesapp.question.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.jfudali.coursesapp.answer.model.Answer;
import com.jfudali.coursesapp.quiz.model.Quiz;
import com.jfudali.coursesapp.quiz.view.QuizView;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "question")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(QuizView.Public.class)
    private Integer idquestion;
    @JsonView(QuizView.Public.class)
    private String text;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "question_idquestion", nullable = false)
    @JsonView(QuizView.Public.class)
    private List<Answer> answers;
}
