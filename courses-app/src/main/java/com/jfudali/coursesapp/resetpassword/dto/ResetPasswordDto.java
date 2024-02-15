package com.jfudali.coursesapp.resetpassword.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResetPasswordDto {
    @NotBlank(message = "E-mail cannot be empty")
    @NotNull(message = "E-mail cannot be null")
    @Email(message = "Provided value is not a valid e-mail")
    private String email;
}
