package com.pokemon.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pokemon.entities.Type;

@Repository
public interface TypeRepository extends JpaRepository<Type, Long> {
    boolean existsByName(String name);

    Optional<Type> findByNameIgnoreCase(String name);

    List<Long> findAllIds();
}
