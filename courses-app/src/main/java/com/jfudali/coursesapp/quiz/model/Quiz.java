package com.jfudali.coursesapp.quiz.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.jfudali.coursesapp.lesson.model.Lesson;
import com.jfudali.coursesapp.question.model.Question;
import com.jfudali.coursesapp.quiz.view.QuizView;
import com.jfudali.coursesapp.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "quiz")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private  Integer idquiz;
    @JsonView(QuizView.Public.class)
    private  String title;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "quiz_idquiz", nullable = false)
    @JsonView(QuizView.Public.class)
    private List<Question> questions = new ArrayList<>();
    @OneToOne(mappedBy = "quiz")
    @JsonIgnore
    private Lesson lesson;
    @ManyToMany(cascade = CascadeType.PERSIST, mappedBy = "passedQuizzes")
    @JsonIgnore
    private Set<User> examinee;
}
