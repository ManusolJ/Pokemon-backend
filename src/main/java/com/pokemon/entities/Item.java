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
    @NotNull
    @EqualsAndHashCode.Include
    private Long id;

    /**
     * The name of the item.
     */
    @Column(name = "name", nullable = false, length = 64)
    @NotNull
    @Length(max = 64)
    private String name;

    /**
     * The description of the item.
     */
    @Column(name = "description", nullable = false, length = 255)
    @NotNull
    @Length(max = 255)
    private String description;
}
