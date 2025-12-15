package com.pokemon.utils.mappers.move;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.pokemon.dtos.rest.move.MoveRestDto;
import com.pokemon.dtos.rest.resource.EffectTextRestDto;
import com.pokemon.dtos.rest.resource.FlavorTextRestDto;
import com.pokemon.dtos.rest.resource.PokeApiResource;
import com.pokemon.entities.Move;
import com.pokemon.utils.enums.DamageClass;
import com.pokemon.utils.mappers.mapper.BaseMapper;
import com.pokemon.utils.mappers.mapper.BaseMapperConfig;

@Mapper(componentModel = "spring", config = BaseMapperConfig.class)
public interface MoveApiMapper extends BaseMapper<Move, MoveRestDto> {

    @Mapping(target = "type", ignore = true)
    @Mapping(target = "shortEffect", source = "effectEntries", qualifiedByName = "getEffectEntry")
    @Mapping(target = "flavorText", source = "flavorTextEntries", qualifiedByName = "getFlavorTextEntry")
    @Mapping(target = "damageClass", source = "damageClass", qualifiedByName = "damageClassFromString")
    Move toEntity(MoveRestDto dto);

    @Named("damageClassFromString")
    static DamageClass damageClassFromString(PokeApiResource damageClassResource) {
        if (damageClassResource == null || damageClassResource.getName() == null) {
            return null;
        }

        return DamageClass.fromString(damageClassResource.getName());
    }

    @Named("getEffectEntry")
    static String getEffectEntry(List<EffectTextRestDto> effectEntries) {
        if (effectEntries == null || effectEntries.isEmpty()) {
            return null;
        }

        for (EffectTextRestDto entry : effectEntries) {
            if (entry.getLanguage() != null && "en".equals(entry.getLanguage().getName())) {
                final String effect = entry.getEffect() != null ? entry.getEffect() : entry.getShortEffect();
                return effect;
            }
        }

        return null;
    }

    @Named("getFlavorTextEntry")
    static String getFlavorTextEntry(List<FlavorTextRestDto> flavorTextEntries) {
        if (flavorTextEntries == null || flavorTextEntries.isEmpty()) {
            return null;
        }

        for (FlavorTextRestDto entry : flavorTextEntries) {
            if (entry.getLanguage() != null && entry.getLanguage().getName().equals("en")) {
                return entry.getFlavorText();
            }
        }

        return null;
    }
}
