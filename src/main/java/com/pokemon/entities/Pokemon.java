package com.pokemon.entities;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Represents an individual pokemon with various attributes and stats.
 */
@Entity
@Table(name = "pokemon")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Pokemon {

    /**
     * The unique identifier for the pokemon.
     */
    @Id
    @EqualsAndHashCode.Include
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    /**
     * The species identifier this pokemon belongs to.
     */
    @NotNull
    @Column(name = "species_id", nullable = false)
    private Long speciesId;

    /**
     * The name of the pokemon form, if any.
     */
    @Size(max = 50)
    @Column(name = "form_name", length = 50)
    private String formName = null;

    /**
     * Indicates if this is the default form of the species.
     */
    @Column(name = "is_default", nullable = false)
    private boolean isDefault = true;

    /**
     * The order of this pokemon form, default to 1.
     */
    @Column(name = "order_in_species", nullable = false)
    @NotNull
    private Integer orderInSpecies = 1;

    /**
     * The primary type identifier for this pokemon.
     */
    @Column(name = "type1_id", nullable = false)
    @NotNull
    private Long type1Id;

    /**
     * The secondary type identifier for this pokemon, if any.
     */
    @Column(name = "type2_id")
    private Long type2Id = null;

    /**
     * The height of the pokemon in meters.
     */
    @Column(name = "height", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal height;

    /**
     * The weight of the pokemon in kilograms.
     */
    @Column(name = "weight", nullable = false, precision = 6, scale = 2)
    @NotNull
    private BigDecimal weight;

    /**
     * The base hp stat of the pokemon.
     */
    @Column(name = "hp", nullable = false)
    @NotNull
    private Integer hp;

    /**
     * The base attack stat of the pokemon.
     */
    @Column(name = "attack", nullable = false)
    @NotNull
    private Integer attack;

    /**
     * The base defense stat of the pokemon.
     */
    @Column(name = "defense", nullable = false)
    @NotNull
    private Integer defense;

    /**
     * The base special attack stat of the pokemon.
     */
    @Column(name = "special_attack", nullable = false)
    @NotNull
    private Integer specialAttack;

    /**
     * The base special defense stat of the pokemon.
     */
    @Column(name = "special_defense", nullable = false)
    @NotNull
    private Integer specialDefense;

    /**
     * The base speed stat of the pokemon.
     */
    @Column(name = "speed", nullable = false)
    @NotNull
    private Integer speed;

    /**
     * Abilities available to each pokemon.
     */
    @ManyToMany
    @OneToMany(mappedBy = "pokemon", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PokemonAbility> pokemonAbilities = new HashSet<>();

    /**
     * Learnable moves by each pokemon.
     */
    @ManyToMany
    @JoinTable(name = "pokemon_moves", joinColumns = @JoinColumn(name = "pokemon_id"), inverseJoinColumns = @JoinColumn(name = "move_id"))
    private Set<Move> moves = new HashSet<>();

    @PrePersist
    private void validateStats() {
        if (hp < 0 || attack < 0 || defense < 0 || specialAttack < 0 || specialDefense < 0 || speed < 0) {
            throw new IllegalStateException("Base stats cannot be negative");
        }
    }
}
