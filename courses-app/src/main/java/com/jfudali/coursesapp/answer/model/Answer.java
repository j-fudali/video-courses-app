package com.jfudali.coursesapp.answer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.jfudali.coursesapp.question.model.Question;
import com.jfudali.coursesapp.quiz.view.QuizView;
import com.jfudali.coursesapp.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "answer")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(QuizView.Public.class)
    private Integer idanswer;
    @JsonView(QuizView.Public.class)
    private String text;
    @Column(name = "is_correct")
    @JsonView(QuizView.Creator.class)
    private boolean isCorrect;
    public boolean getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(boolean correct) {
        isCorrect = correct;
    }
}
