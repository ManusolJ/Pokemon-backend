package com.pokemon.utils.mappers.species;

import org.mapstruct.Mapper;

import com.pokemon.dtos.pokemon.species.ReadPokemonSpeciesDto;
import com.pokemon.entities.PokemonSpecies;
import com.pokemon.utils.mappers.mapper.BaseMapper;
import com.pokemon.utils.mappers.mapper.BaseMapperConfig;

@Mapper(componentModel = "spring", config = BaseMapperConfig.class)
public interface PokemonSpeciesMapper extends BaseMapper<PokemonSpecies, ReadPokemonSpeciesDto> {

    @Override
    ReadPokemonSpeciesDto toDto(PokemonSpecies entity);
}
