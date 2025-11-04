package com.pokemon.utils.mappers;

import org.mapstruct.Mapper;

import com.pokemon.dtos.pokemon.ReadTypeDto;
import com.pokemon.dtos.rest.TypeRestDto;
import com.pokemon.entities.Type;

@Mapper(componentModel = "spring")
public interface TypeMapper {

    Type toEntity(TypeRestDto typeRestDto);

    ReadTypeDto toReadDto(Type type);
}
