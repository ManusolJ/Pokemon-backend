package com.pokemon.utils.mappers;

import org.mapstruct.Mapper;

import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.pokemon.dtos.pokemon.type.ReadTypeDto;
import com.pokemon.dtos.rest.TypeRestDto;
import com.pokemon.entities.Type;

@Mapper(componentModel = "spring", config = MapperConfig.class)
public interface TypeMapper extends BaseMapper<Type, ReadTypeDto> {

    @Override
    ReadTypeDto toDto(Type type);

    Type toEntity(TypeRestDto typeRestDto);
}
