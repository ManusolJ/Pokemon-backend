package com.pokemon.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pokemon.entities.User;

@Repository
public interface UserRepository extends BaseRepository<User, Long> {

    boolean existsByUsername(String username);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE User u SET u.active = :active WHERE u.id IN :ids")
    int updateActiveStatusByIds(boolean active, Iterable<Long> ids);
}
