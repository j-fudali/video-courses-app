package com.jfudali.coursesapp.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ChangePasswordDto {
    @NotBlank(message = "New password cannot be empty")
    @NotNull(message = "New password cannot be null")
    private String oldPassword;
    @NotBlank(message = "New password cannot be empty")
    @NotNull(message = "New password cannot be null")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$", message = "Password is incorrect")
    private String newPassword;
}
