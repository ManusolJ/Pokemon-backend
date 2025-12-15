package com.pokemon.utils.mappers.type;

import org.mapstruct.Mapper;

import com.pokemon.dtos.rest.types.TypeRestDto;
import com.pokemon.entities.Type;
import com.pokemon.utils.mappers.mapper.BaseMapper;
import com.pokemon.utils.mappers.mapper.BaseMapperConfig;

@Mapper(componentModel = "spring", config = BaseMapperConfig.class)
public interface TypeApiMapper extends BaseMapper<Type, TypeRestDto> {
    Type toEntity(TypeRestDto dto);
}
