package com.pokemon.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Represents an item.
 */
@Entity
@Table(name = "items")
@Data
public class Item {

    /**
     * The unique identifier for the item.
     */
    @Id
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private Long id;

    /**
     * The name of the item.
     */
    @Column(name = "name", nullable = false, length = 64)
    private String name;

    /**
     * The description of the item.
     */
    @Column(name = "description", nullable = false, length = 255)
    private String description;
}
