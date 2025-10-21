package com.pokemon.entities;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Entity representing a user's Pokémon with various attributes such as level,
 * EVs, IVs, ability, item, and nature.
 */
@Entity
@Table(name = "user_pokemon")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserPokemon {

    /**
     * Unique identifier for the UserPokemon entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    @EqualsAndHashCode.Include
    private Long id;

    /**
     * Identifier for the user who owns this Pokémon.
     */
    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * Identifier for the species of the Pokémon.
     */
    @NotNull
    @Column(name = "pokemon_id", nullable = false)
    private Long pokemonId;

    /**
     * Nickname given to the Pokémon by the user.
     */
    @Length(max = 30)
    @Column(name = "nickname", length = 30)
    private String nickname = null;

    /**
     * Identifier for the ability of the Pokémon.
     */
    @Column(name = "ability_id")
    private Long abilityId = null;

    /**
     * Identifier for the item held by the Pokémon.
     */
    @Column(name = "item_id")
    private Long itemId = null;

    /**
     * Identifier for the nature of the Pokémon.
     */
    @Column(name = "nature_id")
    private Long natureId = null;

    /**
     * Identifier for the Tera Type of the Pokémon.
     */
    @Column(name = "tera_type_id")
    private Long teraTypeId = null;

    /**
     * Indicates if the Pokémon is shiny. Defaults to false.
     */
    @Column(name = "is_shiny", nullable = false)
    @NotNull
    private boolean isShiny = false;

    /**
     * Level of the Pokémon, ranging from 1 to 100. Defaults to 50.
     */
    @Column(name = "level", nullable = false)
    @NotNull
    @Min(1)
    @Max(100)
    private Integer level = 50;

    /**
     * Effort Values (EVs) for the Pokémon's stats, ranging from 0 to 252.
     * Defaults to 0.
     * The total EVs across all stats can't exceed 510.
     */
    @Column(name = "hp_ev")
    @Min(0)
    @Max(252)
    private Integer hpEv = 0;

    @Column(name = "attack_ev")
    @Min(0)
    @Max(252)
    private Integer attackEv = 0;

    @Column(name = "defense_ev")
    @Min(0)
    @Max(252)
    private Integer defenseEv = 0;

    @Column(name = "sp_attack_ev")
    @Min(0)
    @Max(252)
    private Integer spAttackEv = 0;

    @Column(name = "sp_defense_ev")
    @Min(0)
    @Max(252)
    private Integer spDefenseEv = 0;

    @Column(name = "speed_ev")
    @Min(0)
    @Max(252)
    private Integer speedEv = 0;

    /**
     * Individual Values (IVs) for the Pokémon's stats, ranging from 0 to 31.
     * Defaults to 31.
     */
    @Column(name = "hp_iv")
    @Min(0)
    @Max(31)
    private Integer hpIv = 31;

    @Column(name = "attack_iv")
    @Min(0)
    @Max(31)
    private Integer attackIv = 31;

    @Column(name = "defense_iv")
    @Min(0)
    @Max(31)
    private Integer defenseIv = 31;

    @Column(name = "sp_attack_iv")
    @Min(0)
    @Max(31)
    private Integer spAttackIv = 31;

    @Column(name = "sp_defense_iv")
    @Min(0)
    @Max(31)
    private Integer spDefenseIv = 31;

    @Column(name = "speed_iv")
    @Min(0)
    @Max(31)
    private Integer speedIv = 31;

    @ManyToMany
    @JoinTable(name = "user_pokemon_moves", joinColumns = @JoinColumn(name = "user_pokemon_id"), inverseJoinColumns = @JoinColumn(name = "move_id"))
    @Size(max = 4)
    private Set<Move> moves = new HashSet<>();

    /**
     * Lifecycle method to ensure that the level, EVs, and IVs are within valid
     * ranges before persisting or updating the entity.
     * Sets default values if the provided values are out of range.
     */
    @PrePersist
    @PreUpdate
    private void validate() {
        // Non-null guards so Bean Validation can run, but we also fail early here
        if (level == null)
            throw new IllegalStateException("level is required");

        // Total EVs must be <= 510
        int totalEvs = n(hpEv) + n(attackEv) + n(defenseEv) + n(spAttackEv) + n(spDefenseEv) + n(speedEv);
        if (totalEvs > 510) {
            throw new IllegalStateException("Total EVs must be <= 510 (was " + totalEvs + ")");
        }
    }

    private static int n(Integer v) {
        return v == null ? 0 : v;
    }

}
