package com.pokemon.entities.CompositeIDs;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Embeddable
@Data
@EqualsAndHashCode
public class PokemonAbilityId {

    @Column(name = "pokemon_id", nullable = false)
    private Long pokemonId;

    @Column(name = "ability_id", nullable = false)
    private Long abilityId;
}
