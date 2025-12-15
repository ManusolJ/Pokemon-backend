package com.pokemon.utils.mappers.pokemon;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.pokemon.dtos.rest.pokemon.PokemonRestDto;
import com.pokemon.dtos.rest.pokemon.StatRestDto;
import com.pokemon.entities.Pokemon;
import com.pokemon.utils.mappers.mapper.BaseMapper;
import com.pokemon.utils.mappers.mapper.BaseMapperConfig;

@Mapper(componentModel = "spring", config = BaseMapperConfig.class)
public interface PokemonApiMapper extends BaseMapper<Pokemon, PokemonRestDto> {

    @Mapping(target = "moves", ignore = true)
    @Mapping(target = "species", ignore = true)
    @Mapping(target = "primaryType", ignore = true)
    @Mapping(target = "secondaryType", ignore = true)
    @Mapping(target = "pokemonAbilities", ignore = true)
    @Mapping(target = "formName", source = "formName.name")
    Pokemon toEntity(PokemonRestDto dto);

    @Override
    @Mapping(target = "moves", ignore = true)
    PokemonRestDto toDto(Pokemon entity);

    @AfterMapping
    default void mapStats(PokemonRestDto dto, @MappingTarget Pokemon entity) {
        if (dto.getStats() != null) {
            for (StatRestDto statDto : dto.getStats()) {
                int baseStat = statDto.getBaseStat();
                String statName = statDto.getStat().getName();

                switch (statName) {
                    case "hp" -> entity.setHp(baseStat);
                    case "attack" -> entity.setAttack(baseStat);
                    case "defense" -> entity.setDefense(baseStat);
                    case "special-attack" -> entity.setSpecialAttack(baseStat);
                    case "special-defense" -> entity.setSpecialDefense(baseStat);
                    case "speed" -> entity.setSpeed(baseStat);
                    default -> throw new IllegalArgumentException("Unknown stat name: " + statName);
                }
                ;
            }
        }
    }

}
