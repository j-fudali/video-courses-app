package com.jfudali.coursesapp.user.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.jfudali.coursesapp.answer.model.Answer;
import com.jfudali.coursesapp.ownership.model.Ownership;
import com.jfudali.coursesapp.quiz.model.Quiz;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jfudali.coursesapp.course.model.Course;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer iduser;
    private String firstname;
    private String lastname;
    private String email;
    @JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING)
    private EUserType type;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Ownership> ownerships;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "passed_quiz", joinColumns = @JoinColumn(name = "user_iduser", referencedColumnName = "iduser"), inverseJoinColumns = @JoinColumn(name = "quiz_idquiz", referencedColumnName = "idquiz"))
    private List<Quiz> quizzes;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(type.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
