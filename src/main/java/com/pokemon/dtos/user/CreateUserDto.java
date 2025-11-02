package com.pokemon.dtos.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateUserDto {

    @NotBlank
    @Size(min = 3, max = 20)
    @Pattern(regexp = "[A-Za-z0-9_.!-]+$")
    private String username;

    @NotBlank
    @Size(min = 8, max = 20)
    private String password;
}
