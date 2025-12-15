package com.pokemon.repositories;

import org.springframework.stereotype.Repository;

import com.pokemon.entities.Item;

@Repository
public interface ItemRepository extends BaseRepository<Item, Long> {

}
