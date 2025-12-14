package com.pokemon.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Represents an elemental typing of a pokemon.
 */
@Data
@Entity
@Table(name = "types")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Type {

    /**
     * The unique identifier for the type.
     */
    @Id
    @EqualsAndHashCode.Include
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    /**
     * The name of the type.
     */
    @NotBlank
    @Size(max = 20)
    @Column(name = "name", nullable = false, unique = true, length = 20)
    private String name;
}
