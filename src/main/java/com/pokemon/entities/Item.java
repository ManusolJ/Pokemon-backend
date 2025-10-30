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
 * Represents an item.
 */
@Entity
@Table(name = "items")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Item {

    /**
     * The unique identifier for the item.
     */
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @EqualsAndHashCode.Include
    private Long id;

    /**
     * The name of the item.
     */
    @NotBlank
    @Size(max = 64)
    @Column(name = "name", nullable = false, length = 64)
    private String name;

    /**
     * The description of the item.
     */
    @NotBlank
    @Size(max = 255)
    @Column(name = "description", nullable = false, length = 255)
    private String description;
}
