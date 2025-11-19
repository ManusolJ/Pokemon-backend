package com.pokemon.repositories;

import org.springframework.stereotype.Repository;

import com.pokemon.entities.User;

@Repository
public interface UserRepository extends BaseRepository<User, Long> {

    boolean existsByUsername(String username);
}
