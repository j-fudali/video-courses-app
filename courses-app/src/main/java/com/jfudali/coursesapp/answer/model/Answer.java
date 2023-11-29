package com.jfudali.coursesapp.answer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jfudali.coursesapp.question.model.Question;
import jakarta.persistence.*;
import lombok.*;

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
    private Integer idanswer;
    private String text;
    @Column(name = "is_correct")
    private boolean isCorrect;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "question_idquestion")
    private Question question;

    public boolean getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(boolean correct) {
        isCorrect = correct;
    }
}
