package com.jfudali.coursesapp.course.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import com.jfudali.coursesapp.lesson.model.Lesson;
import com.jfudali.coursesapp.ownership.model.Ownership;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.transaction.annotation.Transactional;

import com.jfudali.coursesapp.category.model.Category;
import com.jfudali.coursesapp.user.model.User;

@Entity
@Table(name = "course")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Course {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idcourse;
    private String name;
    @Column(precision = 10, scale = 2)
    private BigDecimal cost;
    @ManyToOne
    @JoinColumn(name = "category_idcategory", referencedColumnName = "idcategory")
    private Category category;
    @ManyToOne
    @JoinColumn(name = "user_iduser", referencedColumnName = "iduser")
    private User creator;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "course")
    private Set<Ownership> ownerships;
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Lesson> lessons;

}
