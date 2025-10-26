package com.pokemon.entities;

import java.time.LocalDateTime;

import org.hibernate.validator.constraints.Length;

import com.pokemon.utils.enums.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Represents a user in the system.
 */
@Entity
@Table(name = "users")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    /**
     * The unique identifier for the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    @EqualsAndHashCode.Include
    private Long id;

    /**
     * The username of the user.
     */
    @Column(name = "username", nullable = false, unique = true, length = 20)
    @NotNull
    @NotBlank
    @Length(min = 3, max = 20)
    private String username;

    /**
     * The password of the user.
     */
    @Column(name = "password_hash", nullable = false)
    @NotNull
    @NotBlank
    @Length(min = 8, max = 20)
    private String password;

    /**
     * The role of the user.
     */
    @Column(name = "user_role", nullable = false)
    @NotNull
    private UserRole role = UserRole.USER;

    /**
     * Account active status.
     */
    @Column(name = "is_active", nullable = false)
    @NotNull
    private boolean isActive = true;

    /**
     * Timestamp when the user was created.
     */
    @Column(name = "created_at", nullable = false, updatable = false, insertable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp when the user was last updated.
     */
    @Column(name = "updated_at", nullable = false, updatable = false, insertable = false)
    private LocalDateTime updatedAt;
}
