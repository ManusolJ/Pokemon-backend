package com.pokemon.repositories;

import org.springframework.stereotype.Repository;

import com.pokemon.entities.PokemonSpecies;

@Repository
public interface PokemonSpeciesRepository extends BaseRepository<PokemonSpecies, Long> {

}
