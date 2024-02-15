package com.jfudali.coursesapp.resetpassword.controller;

import com.jfudali.coursesapp.dto.ResponseMessage;
import com.jfudali.coursesapp.resetpassword.dto.ResetPasswordConfirmDto;
import com.jfudali.coursesapp.resetpassword.dto.ResetPasswordDto;
import com.jfudali.coursesapp.resetpassword.service.ResetPasswordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reset-password")
@RequiredArgsConstructor
@Validated
public class ResetPasswordController {
    private final ResetPasswordService resetPasswordService;
    @PostMapping
    public ResponseEntity<ResponseMessage> resetPassword(@Valid  @RequestBody ResetPasswordDto resetPasswordDto){
        return new ResponseEntity<>(new ResponseMessage(this.resetPasswordService.resetPassword(resetPasswordDto.getEmail())),
                                    HttpStatus.OK);
    }
    @PostMapping("/confirm")
    public ResponseEntity<ResponseMessage> resetPasswordConfirm(@Valid @RequestParam(name =
            "token", required = true) String token, @Valid @RequestBody ResetPasswordConfirmDto resetPasswordConfirmDto){
        return new ResponseEntity<>(new ResponseMessage(resetPasswordService.confirmResetPassword(token, resetPasswordConfirmDto.getPassword())),
                                    HttpStatus.OK);
    }
}
