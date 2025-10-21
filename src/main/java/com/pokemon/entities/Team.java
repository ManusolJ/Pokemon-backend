package com.pokemon.entities;

import java.time.LocalDateTime;

import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Represents a pokemon team.
 */
@Entity
@Table(name = "teams")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Team {

    /**
     * The unique identifier for the team.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    @EqualsAndHashCode.Include
    private Long id;

    /**
     * The ID of the user who owns the team.
     */
    @Column(name = "user_id", nullable = false)
    @NotNull
    private Long userId;

    /**
     * The name of the team.
     */
    @Column(name = "name", nullable = false, length = 100)
    @NotNull
    @Length(min = 3, max = 100)
    private String name;

    /**
     * Timestamp when the team was created.
     */
    @Column(name = "created_at", nullable = false, updatable = false, insertable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp when the team was last updated.
     */
    @Column(name = "updated_at", nullable = false, insertable = false)
    private LocalDateTime updatedAt;
}
