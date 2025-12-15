package com.pokemon.entities;

import com.pokemon.entities.CompositeIDs.PokemonAbilityId;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * Represents the association between a Pokemon and its Abilities, including
 * details such as slot and whether the ability is hidden.
 */
@Data
@Entity
@Table(name = "pokemon_abilities")
public class PokemonAbility {

    /**
     * Composite primary key consisting of pokemonId and abilityId.
     */
    @EmbeddedId
    private PokemonAbilityId id = new PokemonAbilityId();

    /**
     * The pokemon associated with this ability.
     */
    @MapsId("pokemonId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pokemon_id", nullable = false)
    private Pokemon pokemon;

    /**
     * The ability associated with this pokemon.
     */
    @MapsId("abilityId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ability_id", nullable = false)
    private Ability ability;

    /**
     * The slot number of the ability for the pokemon (1 or 2 for regular abilities,
     * 3 for hidden ability).
     */
    @Min(1)
    @Column(name = "slot", nullable = false)
    private byte slot = 1;

    /**
     * Indicates if this ability is the hidden ability for the pokemon.
     */
    @Column(name = "is_hidden", nullable = false)
    private boolean hidden = false;
}
