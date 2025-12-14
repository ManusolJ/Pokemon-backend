package com.pokemon.repositories;

import org.springframework.stereotype.Repository;

import com.pokemon.entities.Ability;

@Repository
public interface AbilityRepository extends BaseRepository<Ability, Long> {

}
