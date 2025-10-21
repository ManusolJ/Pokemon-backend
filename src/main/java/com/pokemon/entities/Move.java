package com.pokemon.entities;

import org.hibernate.validator.constraints.Length;

import com.pokemon.utils.enums.DamageClass;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Represents a move made by a pokemon.
 */
@Entity
@Table(name = "moves")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Move {

    /**
     * The unique identifier for the move.
     */
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @NotNull
    @EqualsAndHashCode.Include
    private Long id;

    /**
     * The name of the move.
     */
    @Column(name = "name", nullable = false, unique = true, length = 64)
    @NotNull
    @Length(max = 64)
    private String name;

    /**
     * The type identifier for the move.
     */
    @Column(name = "type_id", nullable = false)
    @NotNull
    private Long typeId;

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
    @Column(name = "pp", nullable = false)
    @NotNull
    private Integer pp;

    /**
     * The priority of the move.
     */
    @Column(name = "priority", nullable = false)
    @NotNull
    private Integer priority = 0;

    /**
     * The damage class of the move.
     */
    @Column(name = "damage_class", nullable = false)
    @NotNull
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
    @Column(name = "effect", nullable = false)
    @NotNull
    @Lob
    private String effect;
}
