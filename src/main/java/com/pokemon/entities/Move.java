package com.pokemon.entities;

import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import com.pokemon.utils.enums.DamageClass;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type", nullable = false)
    private Type type;

    /**
     * The accuracy of the move.
     */
    @Column(name = "accuracy")
    private Byte accuracy = null;

    /**
     * The power of the move.
     */
    @Column(name = "power")
    private Byte power = null;

    /**
     * The PP (Power Points) of the move.
     */
    @Column(name = "pp")
    private Byte pp = null;

    /**
     * The priority of the move.
     */
    @NotNull
    @Column(name = "priority", nullable = false)
    private Byte priority = 0;

    /**
     * The damage class of the move.
     */

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "damage_class", nullable = false, columnDefinition = "damage_class_enum")
    private DamageClass damageClass;

    /**
     * The effect chance of the move.
     */
    @Column(name = "effect_chance")
    private Byte effectChance = null;

    /**
     * The short effect description of the move.
     */
    @Column(name = "short_effect", columnDefinition = "LONGTEXT")
    private String shortEffect = null;

    /**
     * The detailed effect description of the move.
     */
    @Column(name = "flavor_text", columnDefinition = "LONGTEXT")
    private String flavorText = null;
}
