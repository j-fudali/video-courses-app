package com.jfudali.coursesapp.quiz.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private  Integer idquiz;
    private  String title;
    @JsonIgnore
    @OneToOne(mappedBy = "quiz")
    private Lesson lesson;
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL)
    private List<Question> questions;
}
