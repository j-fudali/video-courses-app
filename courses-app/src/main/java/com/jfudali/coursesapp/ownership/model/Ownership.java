package com.jfudali.coursesapp.ownership.model;

import com.jfudali.coursesapp.course.model.Course;
import com.jfudali.coursesapp.user.model.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ownership")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Ownership {
    @EmbeddedId
    private OwnershipKey id;
    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "course_idcourse")
    private Course course;
    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_iduser")
    private User user;
    @Column(name = "is_completed")
    private boolean isCompleted;

    public boolean getIsCompleted(){
        return isCompleted;
    }
    public void setIsCompleted(boolean isCompleted){
        this.isCompleted = isCompleted;
    }
}
