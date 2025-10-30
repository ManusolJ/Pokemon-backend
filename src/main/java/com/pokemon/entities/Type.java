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
@Entity
@Table(name = "types")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Type {

    /**
     * The unique identifier for the type.
     */
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @EqualsAndHashCode.Include
    private Long id;

    /**
     * The name of the type.
     */
    @NotBlank
    @Size(max = 20)
    @Column(name = "name", nullable = false, unique = true, length = 20)
    private String name;
}
