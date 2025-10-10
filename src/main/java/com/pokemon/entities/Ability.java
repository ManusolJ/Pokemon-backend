package com.pokemon.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Represents a Pokemon ability.
 */
@Entity
@Table(name = "abilities")
@Data
public class Ability {

    /**
     * The unique identifier for the ability.
     */
    @Id
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private Long id;

    /**
     * The name of the ability.
     */
    @Column(name = "name", nullable = false, length = 64)
    private String name;

    /**
     * The description of the ability.
     */
    @Column(name = "description", nullable = false, length = 255)
    private String description;
}
