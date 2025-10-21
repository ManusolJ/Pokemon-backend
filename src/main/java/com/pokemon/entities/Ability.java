package com.pokemon.entities;

import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Represents a Pokemon ability.
 */
@Entity
@Table(name = "abilities")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Ability {

    /**
     * The unique identifier for the ability.
     */
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @NotNull
    @EqualsAndHashCode.Include
    private Long id;

    /**
     * The name of the ability.
     */
    @Column(name = "name", nullable = false, length = 64)
    @NotNull
    @Length(max = 64)
    private String name;

    /**
     * The description of the ability.
     */
    @Column(name = "description", nullable = false, length = 255)
    @NotNull
    @Length(max = 255)
    private String description;
}
