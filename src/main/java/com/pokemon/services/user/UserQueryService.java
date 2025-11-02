package com.pokemon.services.user;

import java.time.Instant;
import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pokemon.dtos.user.ReadUserDto;
import com.pokemon.dtos.user.UserStatisticsDto;
import com.pokemon.repositories.UserRepository;
import com.pokemon.utils.enums.UserRole;
import com.pokemon.utils.mappers.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public boolean userExists(long id) {
        return userRepository.existsById(id);
    }

    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username.trim());
    }

    public ReadUserDto getUserById(long id) {
        return userRepository.findById(id).map(userMapper::toReadUserDto)
                .orElseThrow(() -> new IllegalArgumentException(String.format("User with id %d not found",
                        id)));
    }

    public ReadUserDto getUserByUsername(String username) {
        return userRepository.findByUsername(username.trim()).map(userMapper::toReadUserDto)
                .orElseThrow(() -> new IllegalArgumentException(String.format("User with username %d not found",
                        username)));
    }

    public Page<ReadUserDto> getUsers(String username, Collection<UserRole> roles, Boolean active, Instant createdFrom,
            Instant createdTo, Instant updatedFrom, Instant updatedTo, Pageable pageable) {
        if (pageable == null) {
            pageable = PageRequest.of(0, 20, Sort.Direction.ASC, "id");
        }
        return userRepository
                .findUsers(username, roles, active, createdFrom, createdTo, updatedFrom, updatedTo, pageable)
                .map(userMapper::toReadUserDto);
    }

    public UserStatisticsDto getStatistics() {
        long totalUsers = userRepository.count();
        long activeUsers = userRepository.countByActiveTrue();
        long adminUsers = userRepository.countByActiveTrueAndRole(UserRole.ADMIN);
        long regularUsers = userRepository.countByActiveTrueAndRole(UserRole.USER);
        long moderatorUsers = userRepository.countByActiveTrueAndRole(UserRole.MODERATOR);
        long inactiveUsers = totalUsers - activeUsers;

        return new UserStatisticsDto(
                totalUsers,
                activeUsers,
                inactiveUsers,
                adminUsers,
                moderatorUsers,
                regularUsers);
    }
}
