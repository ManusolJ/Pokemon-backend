package com.pokemon.services.user;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import com.pokemon.dtos.user.CreateUserDto;
import com.pokemon.dtos.user.ReadUserDto;
import com.pokemon.dtos.user.UpdateUserDto;
import com.pokemon.entities.User;
import com.pokemon.repositories.UserRepository;
import com.pokemon.utils.enums.UserRole;
import com.pokemon.utils.mappers.UserMapper;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Validated
public class UserCommandService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public ReadUserDto createUser(@Valid CreateUserDto dto) {
        final String rawUsername = dto.getUsername();
        if (rawUsername == null || rawUsername.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is required");
        }
        final String rawPassword = dto.getPassword();
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is required");
        }

        final String username = rawUsername.trim();

        if (userRepository.existsByUsername(username)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    String.format("Username '%s' already exists.", username));
        }

        final User user = new User();
        user.setUsername(username);
        user.setPasswordHash(passwordEncoder.encode(rawPassword));

        final User savedUser = userRepository.save(user);
        return userMapper.toReadUserDto(savedUser);
    }

    @Transactional
    public ReadUserDto updateUser(@Valid UpdateUserDto dto, long id) {
        final User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("User not found with id: %d", id)));

        if (dto.getUsername() != null) {
            final String newUsername = dto.getUsername().trim();
            if (newUsername.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username cannot be blank");
            }
            if (!user.getUsername().equals(dto.getUsername())) {
                if (userRepository.existsByUsername(newUsername)) {
                    throw new ResponseStatusException(
                            HttpStatus.CONFLICT,
                            String.format("Username '%s' already exists.", newUsername));
                }
                user.setUsername(newUsername);
            }
        }

        user.setActive(dto.isActive());

        if (dto.getRole() != null) {
            final UserRole newRole = dto.getRole();
            if (!UserRole.isValidRole(newRole.getKey())) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        String.format("Invalid role: '%s'", newRole.getKey()));
            }
            user.setRole(newRole);
        }

        final User updated = userRepository.save(user);
        return userMapper.toReadUserDto(updated);
    }

    @Transactional
    public void deleteUser(long id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("User not found with id: %d", id));
        }
        userRepository.deleteById(id);
    }

    @Transactional
    public void deleteMultipleUsers(Collection<Long> ids) {
        if (ids == null || ids.isEmpty() || ids.size() == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No user IDs provided for deletion");
        }

        if (ids.stream().anyMatch(Objects::isNull)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Null ID in request");
        }

        final List<Long> uniqueIds = ids.stream().distinct().toList();

        if (uniqueIds.size() == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No valid user IDs provided for deletion");
        }

        userRepository.deleteAllByIdInBatch(uniqueIds);
    }

    @Transactional
    public void changePassword(@NotBlank String newPassword, long id) {
        final User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("User not found with id: %d", id)));

        final String trimmed = newPassword.trim();
        if (trimmed.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password cannot be blank");
        }

        user.setPasswordHash(passwordEncoder.encode(trimmed));
        userRepository.save(user);
    }
}
