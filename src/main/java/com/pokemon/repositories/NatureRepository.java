package com.pokemon.repositories;

import org.springframework.stereotype.Repository;

import com.pokemon.entities.Nature;

@Repository
public interface NatureRepository extends BaseRepository<Nature, Long> {

}
