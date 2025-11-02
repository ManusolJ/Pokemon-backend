package com.pokemon.utils.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Getter;

@Getter
public enum UserRole {
    USER("user", "Regular user", 1),
    MODERATOR("moderator", "Content moderator", 2),
    ADMIN("admin", "System administrator", 3);

    private final String key;

    private final String description;

    private final int level;

    private static final Map<String, UserRole> ROLE_MAP = Arrays.stream(values())
            .collect(Collectors.toMap(UserRole::getKey, role -> role));

    UserRole(String key, String description, int level) {
        this.key = key;
        this.description = description;
        this.level = level;
    }

    public static UserRole fromString(String role) {
        if (role == null) {
            return null;
        }

        String normalized = role.trim().toLowerCase();

        try {
            return UserRole.valueOf(normalized.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ROLE_MAP.get(normalized);
        }
    }

    public static boolean isValidRole(String role) {
        return fromString(role) != null;
    }

    public static Set<String> getValidRoleNames() {
        return Arrays.stream(values())
                .map(UserRole::name)
                .collect(Collectors.toSet());
    }

    public static Set<String> getValidRoleKeys() {
        return Arrays.stream(values())
                .map(UserRole::getKey)
                .collect(Collectors.toSet());
    }

    public boolean hasPermission(UserRole requiredRole) {
        return this.level >= requiredRole.level;
    }
}
