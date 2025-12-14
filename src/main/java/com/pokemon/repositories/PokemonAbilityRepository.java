package com.pokemon.repositories;

import org.springframework.stereotype.Repository;

import com.pokemon.entities.PokemonAbility;
import com.pokemon.entities.CompositeIDs.PokemonAbilityId;

@Repository
public interface PokemonAbilityRepository extends BaseRepository<PokemonAbility, PokemonAbilityId> {

}
