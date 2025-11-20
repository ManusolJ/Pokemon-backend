package com.pokemon.repositories;

import org.springframework.stereotype.Repository;

import com.pokemon.entities.Type;

@Repository
public interface TypeRepository extends BaseRepository<Type, Long> {

    public Type findByNameIgnoreCase(String name);
}
