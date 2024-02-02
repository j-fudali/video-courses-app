package com.jfudali.coursesapp.course.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jfudali.coursesapp.category.model.Category;
import com.jfudali.coursesapp.user.dto.PublicUserDto;
import com.jfudali.coursesapp.user.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
public class GetAllCoursesDto {
    private Integer idcourse;
    private String name;
    private String description;
    private BigDecimal cost;
    private Category category;
    private PublicUserDto creator;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Boolean isBought;

    public GetAllCoursesDto(Integer idcourse, String name, String description, BigDecimal cost, Category category,
                            User creator, boolean isBought) {
        this.idcourse = idcourse;
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.category = category;
        this.creator = new PublicUserDto(creator.getIduser(),creator.getFirstname(), creator.getLastname());
        this.isBought = isBought;
    }


}
