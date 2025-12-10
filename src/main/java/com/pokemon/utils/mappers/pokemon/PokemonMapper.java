package com.pokemon.utils.mappers.pokemon;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import com.pokemon.dtos.pokemon.pokemon.ReadPokemonDto;
import com.pokemon.dtos.rest.PokemonRestDto;
import com.pokemon.dtos.rest.StatRestDto;
import com.pokemon.dtos.rest.resource.PokeApiResource;
import com.pokemon.entities.Pokemon;
import com.pokemon.utils.mappers.mapper.BaseMapper;
import com.pokemon.utils.mappers.mapper.BaseMapperConfig;

@Mapper(componentModel = "spring", config = BaseMapperConfig.class)
public interface PokemonMapper extends BaseMapper<Pokemon, ReadPokemonDto> {

    @Override
    ReadPokemonDto toDto(Pokemon entity);

    Pokemon toEntity(PokemonRestDto dto, Long type1Id, Long type2Id, Long speciesId, String formName);

    @Named("decimetersToMeters")
    static BigDecimal decimetersToMeters(int height) {
        return BigDecimal.valueOf(height)
                .divide(BigDecimal.TEN, 2, RoundingMode.HALF_UP);
    }

    @Named("hectogramsToKilograms")
    static BigDecimal hectogramsToKilograms(int weight) {
        return BigDecimal.valueOf(weight)
                .divide(BigDecimal.TEN, 2, RoundingMode.HALF_UP);
    }

    @Named("extractStatValue")
    static Integer extractStatValue(StatRestDto[] stats, @Context String statName) {
        if (stats == null) {
            return 0;
        }

        for (StatRestDto stat : stats) {
            PokeApiResource statResource = stat.getStat();
            if (statResource != null && statResource.getName() != null) {
                String name = statResource.getName();
                if ((statName.equals("hp") && "hp".equalsIgnoreCase(name)) ||
                        (statName.equals("attack") && "attack".equalsIgnoreCase(name)) ||
                        (statName.equals("defense") && "defense".equalsIgnoreCase(name)) ||
                        (statName.equals("specialAttack") && "special-attack".equalsIgnoreCase(name)) ||
                        (statName.equals("specialDefense") && "special-defense".equalsIgnoreCase(name)) ||
                        (statName.equals("speed") && "speed".equalsIgnoreCase(name))) {
                    return stat.getBaseStat();
                }
            }
        }
        return 0;
    }
}
