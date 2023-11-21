package com.jfudali.coursesapp.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "Empty firstname")
    @NotNull(message = "Firstname is null")
    @Size(min = 5, max = 70, message = "Invalid firstname: Must be of 3 - 30 characters")
    private String firstname;
    @NotBlank(message = "Empty lastname")
    @NotNull(message = "Lastname is null")
    @Size(min = 5, max = 70, message = "Invalid lastname: Must be of 3 - 30 characters")
    private String lastname;
    @NotBlank(message = "Empty email")
    @NotNull(message = "Email is null")
    @Email(message = "Invalid email")
    private String email;
    @NotBlank(message = "Empty password")
    @NotNull(message = "Password is null")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$", message = "Password is incorrect")
    private String password;
}
