package com.jfudali.coursesapp.resetpassword.repository;

import com.jfudali.coursesapp.resetpassword.model.ResetPassword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResetPasswordRepository extends JpaRepository<ResetPassword,
 Integer> {
    Optional<ResetPassword> findByToken(String token);
}
