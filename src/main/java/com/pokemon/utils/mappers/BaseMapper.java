package com.pokemon.utils.mappers;

public interface BaseMapper<E, D> {

    D toDto(E entity);

    E toEntity(D dto);
}
