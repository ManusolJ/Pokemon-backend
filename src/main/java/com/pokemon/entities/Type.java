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
    @NotNull
    @EqualsAndHashCode.Include
    private Long id;

    /**
     * The name of the type.
     */
    @Column(name = "name", nullable = false, unique = true, length = 20)
    @NotNull
    @Length(max = 20)
    private String name;
}
