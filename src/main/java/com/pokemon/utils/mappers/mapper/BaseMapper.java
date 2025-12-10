package com.pokemon.utils.mappers.mapper;

public interface BaseMapper<E, D> {

    D toDto(E entity);
}
