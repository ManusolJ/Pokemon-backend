package com.pokemon.utils.mappers.item;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.pokemon.dtos.rest.item.ItemRestDto;
import com.pokemon.dtos.rest.resource.EffectTextRestDto;
import com.pokemon.dtos.rest.resource.FlavorTextRestDto;
import com.pokemon.entities.Item;
import com.pokemon.utils.mappers.mapper.BaseMapper;
import com.pokemon.utils.mappers.mapper.BaseMapperConfig;

@Mapper(componentModel = "spring", config = BaseMapperConfig.class)
public interface ItemApiMapper extends BaseMapper<Item, ItemRestDto> {

    @Mapping(target = "effect", source = "effectEntries", qualifiedByName = "getEffectEntry")
    @Mapping(target = "flavorText", source = "flavorTextEntries", qualifiedByName = "getFlavorTextEntry")
    Item toEntity(ItemRestDto dto);

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

    @Named("getEffectEntry")
    static String getEffectEntry(List<EffectTextRestDto> effectEntries) {
        if (effectEntries == null || effectEntries.isEmpty()) {
            return null;
        }

        for (EffectTextRestDto entry : effectEntries) {
            if (entry.getLanguage() != null && entry.getLanguage().getName().equals("en")) {
                return entry.getEffect();
            }
        }

        return null;
    }
}
