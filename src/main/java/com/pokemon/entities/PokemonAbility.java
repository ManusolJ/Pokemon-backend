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

@Entity
@Table(name = "pokemon_abilities")
@Data
public class PokemonAbility {

    @EmbeddedId
    private PokemonAbilityId id = new PokemonAbilityId();

    @MapsId("pokemonId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pokemon_id", nullable = false)
    private Pokemon pokemon;

    @MapsId("abilityId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ability_id", nullable = false)
    private Ability ability;

    @Min(1)
    @Column(name = "slot", nullable = false)
    private int slot = 1;

    @Column(name = "is_hidden", nullable = false)
    private boolean hidden = false;
}
