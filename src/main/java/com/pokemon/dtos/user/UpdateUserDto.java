package com.pokemon.dtos.user;

import com.pokemon.utils.enums.UserRole;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserDto {

    @NotBlank
    @Size(min = 3, max = 20)
    @Pattern(regexp = "[A-Za-z0-9_.!-]+$")
    private String username;

    private boolean active;

    private UserRole role;
}
