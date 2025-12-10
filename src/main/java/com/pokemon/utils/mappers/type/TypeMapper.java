package com.pokemon.utils.mappers.type;

import org.mapstruct.Mapper;

import com.pokemon.dtos.pokemon.type.ReadTypeDto;
import com.pokemon.entities.Type;
import com.pokemon.utils.mappers.mapper.BaseMapper;
import com.pokemon.utils.mappers.mapper.BaseMapperConfig;

@Mapper(componentModel = "spring", config = BaseMapperConfig.class)
public interface TypeMapper extends BaseMapper<Type, ReadTypeDto> {

    @Override
    ReadTypeDto toDto(Type type);
}
