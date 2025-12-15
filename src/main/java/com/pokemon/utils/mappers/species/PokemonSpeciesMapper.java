package com.pokemon.utils.mappers.species;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.pokemon.dtos.pokemon.species.ReadPokemonSpeciesDto;
import com.pokemon.dtos.pokemon.species.ReducedReadPokemonSpeciesDto;
import com.pokemon.entities.PokemonSpecies;
import com.pokemon.utils.mappers.mapper.BaseMapper;
import com.pokemon.utils.mappers.mapper.BaseMapperConfig;

@Mapper(componentModel = "spring", config = BaseMapperConfig.class)
public interface PokemonSpeciesMapper extends BaseMapper<PokemonSpecies, ReadPokemonSpeciesDto> {

    @Override
    @Mapping(target = "previousEvolution", source = "previousEvolution", qualifiedByName = "setPreviousEvolution")
    ReadPokemonSpeciesDto toDto(PokemonSpecies entity);

    ReducedReadPokemonSpeciesDto toReducedDto(PokemonSpecies entity);

    @Named("setPreviousEvolution")
    default ReducedReadPokemonSpeciesDto setPreviousEvolution(PokemonSpecies species) {
        if (species != null) {
            return toReducedDto(species);
        }
        return null;
    }
}
