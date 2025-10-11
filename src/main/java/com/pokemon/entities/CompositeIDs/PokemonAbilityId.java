package com.pokemon.entities.CompositeIDs;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Composite key class for Pokemon
 * and Ability relationship.
 */
@Embeddable
@Data
@EqualsAndHashCode
public class PokemonAbilityId {

    /**
     * The unique identifier for the pokemon.
     */
    @Column(name = "pokemon_id", nullable = false)
    private Long pokemonId;

    /**
     * The unique identifier for the ability.
     */
    @Column(name = "ability_id", nullable = false)
    private Long abilityId;
}
