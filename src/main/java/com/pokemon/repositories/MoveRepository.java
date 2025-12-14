package com.pokemon.repositories;

import org.springframework.stereotype.Repository;

import com.pokemon.entities.Move;

@Repository
public interface MoveRepository extends BaseRepository<Move, Long> {

}
