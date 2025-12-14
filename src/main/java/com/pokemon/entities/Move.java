package com.pokemon.entities;

import com.pokemon.utils.enums.DamageClass;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Represents a move made by a pokemon.
 */
@Data
@Entity
@Table(name = "moves")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Move {

    /**
     * The unique identifier for the move.
     */
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @EqualsAndHashCode.Include
    private Long id;

    /**
     * The name of the move.
     */
    @NotBlank
    @Size(max = 64)
    @Column(name = "name", nullable = false, unique = true, length = 64)
    private String name;

    /**
     * The type identifier for the move.
     */
    @NotNull
    @Column(name = "type_id", nullable = false)
    private Type type;

    /**
     * The accuracy of the move.
     */
    @Column(name = "accuracy")
    private Integer accuracy = null;

    /**
     * The power of the move.
     */
    @Column(name = "power")
    private Integer power = null;

    /**
     * The PP (Power Points) of the move.
     */
    @NotNull
    @Column(name = "pp", nullable = false)
    private Integer pp;

    /**
     * The priority of the move.
     */
    @NotNull
    @Column(name = "priority", nullable = false)
    private Integer priority = 0;

    /**
     * The damage class of the move.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "damage_class", nullable = false)
    private DamageClass damageClass;

    /**
     * The effect chance of the move.
     */
    @Column(name = "effect_chance")
    private Integer effectChance = null;

    /**
     * The short effect description of the move.
     */
    @Column(name = "short_effect", length = 255)
    private String shortEffect = null;

    /**
     * The detailed effect description of the move.
     */
    @Lob
    @NotBlank
    @Column(name = "flavor_text", nullable = false)
    private String flavorText;
}
