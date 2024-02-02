package com.jfudali.coursesapp.question.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.jfudali.coursesapp.answer.model.Answer;
import com.jfudali.coursesapp.quiz.view.QuizView;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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
