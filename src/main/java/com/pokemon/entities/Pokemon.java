package com.pokemon.entities;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Represents an individual pokemon with various attributes and stats.
 */
@Data
@Entity
@Table(name = "pokemon")
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
     * The species this pokemon belongs to.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "species", nullable = false)
    private PokemonSpecies species;

    /**
     * The name of the pokemon species.
     */
    @NotBlank
    @Size(max = 50)
    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

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
     * The order of the pokemon for sorting purposes.
     */
    @Column(name = "order_index", nullable = false)
    @NotNull
    private Integer order;

    /**
     * The primary type identifier for this pokemon.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "primary_type", nullable = false)
    private Type primaryType;

    /**
     * The secondary type identifier for this pokemon, if any.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "secondary_type")
    private Type secondaryType = null;

    /**
     * The height of the pokemon in Decimeters.
     */
    @NotNull
    @Column(name = "height", nullable = false)
    private Integer height;

    /**
     * The weight of the pokemon in Hectograms.
     */
    @NotNull
    @Column(name = "weight", nullable = false)
    private Integer weight;

    /**
     * The base hp stat of the pokemon.
     */
    @NotNull
    @Column(name = "hp", nullable = false)
    private Integer hp;

    /**
     * The base attack stat of the pokemon.
     */
    @NotNull
    @Column(name = "attack", nullable = false)
    private Integer attack;

    /**
     * The base defense stat of the pokemon.
     */
    @NotNull
    @Column(name = "defense", nullable = false)
    private Integer defense;

    /**
     * The base special attack stat of the pokemon.
     */
    @NotNull
    @Column(name = "special_attack", nullable = false)
    private Integer specialAttack;

    /**
     * The base special defense stat of the pokemon.
     */
    @NotNull
    @Column(name = "special_defense", nullable = false)
    private Integer specialDefense;

    /**
     * The base speed stat of the pokemon.
     */
    @NotNull
    @Column(name = "speed", nullable = false)
    private Integer speed;

    /**
     * Abilities available to each pokemon.
     */
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
