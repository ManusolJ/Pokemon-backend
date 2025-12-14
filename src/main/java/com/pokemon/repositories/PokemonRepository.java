package com.pokemon.repositories;

import org.springframework.stereotype.Repository;

import com.pokemon.entities.Pokemon;

@Repository
public interface PokemonRepository extends BaseRepository<Pokemon, Long> {

}
