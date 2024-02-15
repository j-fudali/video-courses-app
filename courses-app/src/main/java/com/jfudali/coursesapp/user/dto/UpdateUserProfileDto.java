package com.jfudali.coursesapp.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserProfileDto {
    @Length(max = 70, message = "First name must include max. 70 characters" +
            "characters")
    private String firstname;
    @Length(max = 100, message = "Last name must include max. 100 characters" +
            " " +
            "characters")
    private String lastname;
}
