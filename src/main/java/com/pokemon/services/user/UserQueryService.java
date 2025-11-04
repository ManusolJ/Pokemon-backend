package com.pokemon.services.user;

import java.time.Instant;
import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import com.pokemon.dtos.user.ReadUserDto;
import com.pokemon.dtos.user.UserStatisticsDto;
import com.pokemon.repositories.UserRepository;
import com.pokemon.utils.enums.UserRole;
import com.pokemon.utils.mappers.UserMapper;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Validated
public class UserQueryService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public boolean userExists(long id) {
        return userRepository.existsById(id);
    }

    public boolean usernameExists(@NotBlank String username) {
        final String normalized = username.trim();
        return userRepository.existsByUsername(normalized);
    }

    public ReadUserDto getUserById(long id) {
        return userRepository.findById(id)
                .map(userMapper::toReadUserDto)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("User with id %d not found", id)));
    }

    public ReadUserDto getUserByUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Username cannot be null or blank");
        }

        return userRepository.findByUsername(
                username)
                .map(userMapper::toReadUserDto)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("User with username '%s' not found", username)));
    }

    public Page<ReadUserDto> getUsers(
            String username,
            Collection<UserRole> roles,
            Boolean active,
            Instant createdFrom,
            Instant createdTo,
            Instant updatedFrom,
            Instant updatedTo,
            Pageable pageable) {

        final Pageable effectivePageable = pageable != null
                ? sanitizePageable(pageable)
                : PageRequest.of(0, 20, Sort.by("id").ascending());

        final String usernameFilter = username != null && !username.isBlank()
                ? username.trim()
                : null;

        return userRepository
                .findUsers(usernameFilter, roles, active, createdFrom, createdTo, updatedFrom, updatedTo,
                        effectivePageable)
                .map(userMapper::toReadUserDto);
    }

    public UserStatisticsDto getStatistics() {
        final long totalUsers = userRepository.count();
        final long activeUsers = userRepository.countByActiveTrue();
        final long adminUsers = userRepository.countByActiveTrueAndRole(UserRole.ADMIN);
        final long regularUsers = userRepository.countByActiveTrueAndRole(UserRole.USER);
        final long moderatorUsers = userRepository.countByActiveTrueAndRole(UserRole.MODERATOR);
        final long inactiveUsers = totalUsers - activeUsers;

        return new UserStatisticsDto(
                totalUsers,
                activeUsers,
                inactiveUsers,
                adminUsers,
                moderatorUsers,
                regularUsers);
    }

    private static Pageable sanitizePageable(Pageable pageable) {
        final int page = Math.max(0, pageable.getPageNumber());
        final int size = Math.min(Math.max(1, pageable.getPageSize()), 100);
        Sort sort = pageable.getSort();
        if (sort == null || sort.isUnsorted()) {
            sort = Sort.by("id").ascending();
        }
        return PageRequest.of(page, size, sort);
    }
}
