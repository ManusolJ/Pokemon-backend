package com.pokemon.services.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
public class UserCommandService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public ReadUserDto createUser(@Valid CreateUserDto dto) throws Exception {
        final String username = dto.getUsername().trim();

        if (userRepository.existsByUsername(username)) {
            throw new Exception(String.format("Error, username '%s' already exists in db.", username));
        }

        final String hashedPassword = passwordEncoder.encode(dto.getPassword());

        User user = userMapper.toEntity(dto);
        user.setPasswordHash(hashedPassword);

        User savedUser = userRepository.save(user);
        return userMapper.toReadUserDto(savedUser);
    }

    @Transactional
    public ReadUserDto updateUser(@Valid UpdateUserDto dto, long id) throws Exception {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new Exception(String.format("User not found with id: %d", id)));

        if (dto.getUsername() != null && !dto.getUsername().trim().isEmpty()
                && !userRepository.existsByUsername(dto.getUsername())) {
            user.setUsername(dto.getUsername().trim());
        }

        user.setActive(dto.isActive());
        if (dto.getRole() != null && !UserRole.isValidRole(dto.getRole().getKey())) {
            user.setRole(dto.getRole());
        }

        User updatedUser = userRepository.save(user);
        return userMapper.toReadUserDto(updatedUser);
    }

    @Transactional
    public boolean deleteUser(long id) throws Exception {
        if (!userRepository.existsById(id)) {
            throw new Exception(String.format("User not found with id: %d", id));
        }

        userRepository.deleteById(id);
        return true;
    }

    @Transactional
    public boolean changePassword(@NotBlank String newPassword, long id) throws Exception {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new Exception(String.format("User not found with id: %d", id)));

        final String hashedPassword = passwordEncoder.encode(newPassword);
        user.setPasswordHash(hashedPassword);
        userRepository.save(user);
        return true;
    }
}
