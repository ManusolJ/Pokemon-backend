package com.pokemon.dtos.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserStatisticsDto {
    private long totalUsers;

    private long activeUsers;

    private long inactiveUsers;

    private long adminUsers;

    private long moderatorUsers;

    private long regularUsers;
}
