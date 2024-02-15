package com.jfudali.coursesapp.resetpassword.service;

import com.jfudali.coursesapp.exceptions.NotFoundException;
import com.jfudali.coursesapp.resetpassword.model.ResetPassword;
import com.jfudali.coursesapp.resetpassword.repository.ResetPasswordRepository;
import com.jfudali.coursesapp.user.model.User;
import com.jfudali.coursesapp.user.repository.UserRepository;
import com.jfudali.coursesapp.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResetPasswordService {
    private final UserService userService;
    private final Environment environment;
    private final JavaMailSender javaMailSender;
    private final ResetPasswordRepository resetPasswordRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    @Transactional
    public String  resetPassword(String userEmail){
        User user = this.userService.getUserByEmail(userEmail);
        String token = UUID.randomUUID().toString();
        LocalDateTime dateTime = LocalDateTime.now().plus(Duration.of(15,
                                                                      ChronoUnit.MINUTES));
        ResetPassword resetPassword =
                ResetPassword.builder().token(token).tokenExpiry(Date.from(dateTime.atZone(
                        ZoneId.systemDefault()).toInstant())).user(user).build();

        resetPasswordRepository.save(resetPassword);
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(environment.getProperty("EMAIL"));
        simpleMailMessage.setTo(userEmail);
        simpleMailMessage.setSubject("Video Courses App - Reset password");
        simpleMailMessage.setText("Click the link to reset you password " +
                                          environment.getProperty(
                                                  "FRONTEND_URL")+"/reset" +
                                          "-password/confirm?token="+token);
        javaMailSender.send(simpleMailMessage);
        return "Check your e-mail " +
                "for link to " +
                "reset " +
                "password";
    }
    @Transactional(noRollbackFor = ResponseStatusException.class)
    public String confirmResetPassword(String token, String password){
        ResetPassword resetPassword =
                resetPasswordRepository.findByToken(token).orElseThrow(() -> new NotFoundException("Provided token not exists"));
        if(resetPassword.getTokenExpiry().compareTo(new Date()) >= 0){
            User user = resetPassword.getUser();
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
            resetPasswordRepository.deleteById(resetPassword.getIdresetPassword());
            return "Password has been set successfully";
        }
        resetPasswordRepository.deleteById(resetPassword.getIdresetPassword());
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token is " +
                "expired");
    }
}
