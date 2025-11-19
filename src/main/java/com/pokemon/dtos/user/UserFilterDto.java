package com.pokemon.dtos.user;

import java.time.Instant;

import com.pokemon.utils.enums.UserRole;

import lombok.Getter;

@Getter
public class UserFilterDto {

    private Long id;

    private String username;

    private String usernameExact;

    private UserRole role;

    private Boolean active;

    private Instant createdAtFrom;

    private Instant createdAtTo;

    private Instant updatedAtFrom;

    private Instant updatedAtTo;
}
