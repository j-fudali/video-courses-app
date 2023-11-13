package com.jfudali.coursesapp.course.model;

import java.math.BigDecimal;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

import com.jfudali.coursesapp.category.model.Category;
import com.jfudali.coursesapp.user.model.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "course")
@NoArgsConstructor
@AllArgsConstructor
@Data
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
    @ManyToMany(mappedBy = "courses")
    private Set<User> users;

}
