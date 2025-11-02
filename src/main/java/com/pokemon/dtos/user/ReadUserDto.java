package com.pokemon.dtos.user;

import java.time.Instant;

import com.pokemon.utils.enums.UserRole;

import lombok.Data;

@Data
public class ReadUserDto {

    private long id;

    private String username;

    private UserRole role;

    private boolean active;

    private Instant createdAt;

    private Instant updatedAt;
}
