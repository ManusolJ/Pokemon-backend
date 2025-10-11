package com.pokemon.entities;

import java.util.ArrayList;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Entity representing a user's Pokémon with various attributes such as level,
 * EVs, IVs, ability, item, and nature.
 */
@Entity
@Table(name = "user_pokemons")
@Data
public class UserPokemon {

    /**
     * Unique identifier for the UserPokemon entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, insertable = false, unique = true)
    private Long id;

    /**
     * Identifier for the user who owns this Pokémon.
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * Identifier for the species of the Pokémon.
     */
    @Column(name = "pokemon_id", nullable = false)
    private Long pokemonId;

    /**
     * Nickname given to the Pokémon by the user.
     */
    @Column(name = "nickname", length = 30)
    private String nickname = null;

    /**
     * Level of the Pokémon, ranging from 1 to 100. Defaults to 50.
     */
    @Column(name = "level", nullable = false)
    private Integer level = 50;

    /**
     * Identifier for the ability of the Pokémon.
     */
    @Column(name = "ability_id")
    private Long ability = null;

    /**
     * Identifier for the item held by the Pokémon.
     */
    @Column(name = "item_id")
    private Long item = null;

    /**
     * Identifier for the nature of the Pokémon.
     */
    @Column(name = "nature_id")
    private Long nature = null;

    /**
     * Effort Values (EVs) for the Pokémon's stats, ranging from 0 to 255.
     * Defaults to 0.
     * The total EVs across all stats can't exceed 510.
     */
    @Column(name = "hp_ev")
    private Integer hpEv = 0;

    @Column(name = "attack_ev")
    private Integer attackEv = 0;

    @Column(name = "defense_ev")
    private Integer defenseEv = 0;

    @Column(name = "special_attack_ev")
    private Integer specialAttackEv = 0;

    @Column(name = "special_defense_ev")
    private Integer specialDefenseEv = 0;

    @Column(name = "speed_ev")
    private Integer speedEv = 0;

    /**
     * Individual Values (IVs) for the Pokémon's stats, ranging from 0 to 31.
     * Defaults to 31.
     */
    @Column(name = "hp_iv")
    private Integer hpIv = 31;

    @Column(name = "attack_iv")
    private Integer attackIv = 31;

    @Column(name = "defense_iv")
    private Integer defenseIv = 31;

    @Column(name = "special_attack_iv")
    private Integer specialAttackIv = 31;

    @Column(name = "special_defense_iv")
    private Integer specialDefenseIv = 31;

    @Column(name = "speed_iv")
    private Integer speedIv = 31;

    /**
     * Lifecycle method to ensure that the level, EVs, and IVs are within valid
     * ranges before persisting or updating the entity.
     * Sets default values if the provided values are out of range.
     */
    @PrePersist
    @PreUpdate
    private void prePersist() {
        if (level == null || level < 1 || level > 100) {
            level = 50;
        }

        ArrayList<Integer> evs = new ArrayList<>();
        evs.add(hpEv);
        evs.add(attackEv);
        evs.add(defenseEv);
        evs.add(specialAttackEv);
        evs.add(specialDefenseEv);
        evs.add(speedEv);
        int totalEvs = 0;
        for (int i = 0; i < evs.size(); i++) {
            if (evs.get(i) == null || evs.get(i) < 0 || evs.get(i) > 255) {
                evs.set(i, 0);
                totalEvs += evs.get(i);
            }
        }

        if (totalEvs > 510) {
            for (int i = 0; i < evs.size(); i++) {
                evs.set(i, 0);
            }
        }

        ArrayList<Integer> ivs = new ArrayList<>();
        ivs.add(hpIv);
        ivs.add(attackIv);
        ivs.add(defenseIv);
        ivs.add(specialAttackIv);
        ivs.add(specialDefenseIv);
        ivs.add(speedIv);

        for (int i = 0; i < ivs.size(); i++) {
            Integer iv = ivs.get(i);
            if (iv == null || iv < 0 || iv > 31) {
                ivs.set(i, 31);
            }
        }

    }
}
