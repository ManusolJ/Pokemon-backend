package com.pokemon.controllers;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pokemon.dtos.user.CreateUserDto;
import com.pokemon.dtos.user.ReadUserDto;
import com.pokemon.dtos.user.UpdateUserDto;
import com.pokemon.dtos.user.UserFilterDto;
import com.pokemon.services.user.UserCommandService;
import com.pokemon.services.user.UserQueryService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {

    private final UserQueryService queryService;
    private final UserCommandService commandService;

    @PostMapping("/filter")
    public ResponseEntity<Page<ReadUserDto>> getUsers(@RequestBody UserFilterDto filter, @NotNull Pageable pageable) {
        final Pageable validPageable = pageable == null ? Pageable.ofSize(20) : pageable;
        Page<ReadUserDto> users = queryService.filterUsers(filter, validPageable);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/count")
    public ResponseEntity<Long> countUsers(@RequestBody UserFilterDto filter) {
        long count = queryService.countWithFilters(filter);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadUserDto> getUserById(@NotNull @PathVariable Long id) {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().build();
        } else {
            ReadUserDto user = queryService.findById(id);
            if (user == null) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.ok(user);
            }
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ReadUserDto> createUser(@Valid @RequestBody CreateUserDto dto) {
        ReadUserDto createdUser = commandService.createUser(dto);
        return createdUser == null ? ResponseEntity.badRequest().build() : ResponseEntity.ok(createdUser);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ReadUserDto> updateUser(@Valid @RequestBody UpdateUserDto dto,
            @NotNull @PathVariable Long id) {
        ReadUserDto updatedUser = commandService.updateUser(dto, id);
        return updatedUser == null ? ResponseEntity.badRequest().build() : ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/admin/deactivate-users")
    public ResponseEntity<String> changeActivationStatusOfMultipleUsers(@RequestBody List<Long> userIds,
            @NotNull @RequestParam Boolean active) {
        String result = commandService.changeActivationStatusOfMultipleUsers(userIds, active);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@NotNull @PathVariable Long id) {
        commandService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/admin/delete-multiple")
    public ResponseEntity<Void> deleteMultipleUsers(@RequestBody List<Long> userIds) {
        commandService.deleteMultipleUsers(userIds);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/change-password/{id}")
    public ResponseEntity<Void> changePassword(@RequestBody String newPassword, @NotNull @PathVariable Long id) {
        commandService.changePassword(newPassword, id);
        return ResponseEntity.ok().build();
    }
}
