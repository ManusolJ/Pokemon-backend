package com.pokemon.utils.mappers.pokemon;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.pokemon.dtos.pokemon.pokemon.ReadPokemonDto;
import com.pokemon.entities.Pokemon;
import com.pokemon.utils.mappers.mapper.BaseMapper;
import com.pokemon.utils.mappers.mapper.BaseMapperConfig;

@Mapper(componentModel = "spring", config = BaseMapperConfig.class)
public interface PokemonMapper extends BaseMapper<Pokemon, ReadPokemonDto> {

    @Override
    @Mapping(target = "primaryType", ignore = true)
    @Mapping(target = "secondaryType", ignore = true)
    ReadPokemonDto toDto(Pokemon entity);

}
