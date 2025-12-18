package com.pokemon.entities;

import java.time.Instant;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import com.pokemon.utils.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Represents a user in the system, persisted to db within the "users" table.
 */
@Data
@Entity
@Table(name = "users")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    /**
     * The unique identifier for the user.
     */
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    /**
     * The username of the user.
     */
    @NotBlank
    @Size(min = 3, max = 20)
    @Column(name = "username", nullable = false, unique = true, length = 20)
    private String username;

    /**
     * The password of the user.
     */
    @NotBlank
    @Column(name = "password_hash", nullable = false, length = 100)
    private String passwordHash;

    /**
     * The role of the user.
     */
    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "user_role", nullable = false)
    private UserRole role = UserRole.USER;

    /**
     * Account active status.
     */
    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    /**
     * Timestamp when the user was created.
     */
    @Column(name = "created_at", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP(6)")
    private Instant createdAt;

    /**
     * Timestamp when the user was last updated.
     */
    @Column(name = "updated_at", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP(6)")
    private Instant updatedAt;
}
