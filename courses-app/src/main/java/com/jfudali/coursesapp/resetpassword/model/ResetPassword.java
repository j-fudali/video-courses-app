package com.jfudali.coursesapp.resetpassword.model;

import com.jfudali.coursesapp.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "reset_password")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResetPassword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idreset_password")
    private Integer idresetPassword;
    private String token;
    @Column(name = "token_expiry")
    private Date tokenExpiry;
    @ManyToOne
    @JoinColumn(name = "user_iduser")
    private User user;
}
