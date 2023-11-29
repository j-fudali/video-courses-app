package com.jfudali.coursesapp.question.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jfudali.coursesapp.answer.model.Answer;
import com.jfudali.coursesapp.quiz.model.Quiz;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "question")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idquestion;
    private String text;
    @ManyToOne
    @JoinColumn(name = "quiz_idquiz")
    @JsonIgnore
    private Quiz quiz;
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<Answer> answers;
}
