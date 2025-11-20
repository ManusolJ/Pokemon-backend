package com.pokemon.utils.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.pokemon.dtos.pokemon.species.ReadPokemonSpeciesDto;
import com.pokemon.dtos.rest.PokemonSpeciesRestDto;
import com.pokemon.entities.PokemonSpecies;

@Mapper(componentModel = "spring", config = BaseMapperConfig.class)
public interface PokemonSpeciesMapper extends BaseMapper<PokemonSpecies, ReadPokemonSpeciesDto> {

    @Override
    ReadPokemonSpeciesDto toDto(PokemonSpecies entity);

    @Mapping(target = "generation", source = "generation")
    @Mapping(target = "description", source = "flavorText")
    @Mapping(target = "evolvesFromSpeciesId", source = "evolvesFromId")
    PokemonSpecies toEntity(PokemonSpeciesRestDto dto, String flavorText, long evolvesFromId, int generation);
}
