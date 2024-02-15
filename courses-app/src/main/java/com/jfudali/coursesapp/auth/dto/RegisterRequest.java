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
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "Empty firstname")
    @NotNull(message = "Firstname is null")
    @Length(max = 70, message = "First name must include max. 70 characters" +
            "characters")
    private String firstname;
    @NotBlank(message = "Empty lastname")
    @NotNull(message = "Lastname is null")
    @Length(max = 100, message = "Last name must include max. 100 characters" +
            " " +
            "characters")
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
