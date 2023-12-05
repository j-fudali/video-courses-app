package com.jfudali.coursesapp.ownership.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OwnershipKey implements Serializable {
    @Column(name = "course_idcourse")
    private Integer courseId;
    @Column(name = "user_iduser")
    private Integer userId;
}
