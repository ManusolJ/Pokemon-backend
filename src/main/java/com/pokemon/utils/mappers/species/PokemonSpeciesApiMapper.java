package com.pokemon.utils.mappers.species;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.pokemon.dtos.rest.PokemonSpeciesRestDto;
import com.pokemon.dtos.rest.TextEntryRestDto;
import com.pokemon.dtos.rest.resource.PokeApiResource;
import com.pokemon.entities.PokemonSpecies;
import com.pokemon.utils.mappers.mapper.BaseMapper;
import com.pokemon.utils.mappers.mapper.BaseMapperConfig;

@Mapper(componentModel = "spring", config = BaseMapperConfig.class)
public interface PokemonSpeciesApiMapper extends BaseMapper<PokemonSpecies, PokemonSpeciesRestDto> {

    @Override
    PokemonSpeciesRestDto toDto(PokemonSpecies entity);

    @Mapping(target = "preevolutionId", source = "preevolutionId")
    @Mapping(target = "generation", source = "generation", qualifiedByName = "generationNameToNumber")
    @Mapping(target = "flavorText", source = "flavorTextEntries", qualifiedByName = "textEntriesToFlavorText")
    PokemonSpecies toEntity(PokemonSpeciesRestDto dto, Long preevolutionId);

    @Named("generationNameToNumber")
    static Integer generationNameToNumber(PokeApiResource generation) {
        String generationName = generation != null ? generation.getName() : null;

        if (generationName == null) {
            return null;
        }

        String roman = generationName.substring("generation-".length());

        return switch (roman.toLowerCase()) {
            case "i" -> 1;
            case "ii" -> 2;
            case "iii" -> 3;
            case "iv" -> 4;
            case "v" -> 5;
            case "vi" -> 6;
            case "vii" -> 7;
            case "viii" -> 8;
            case "ix" -> 9;
            default -> null;
        };
    }

    @Named("textEntriesToFlavorText")
    static String textEntriesToFlavorText(List<TextEntryRestDto> flavorTextEntries) {
        if (flavorTextEntries == null) {
            return null;
        }

        return flavorTextEntries.stream()
                .filter(entry -> entry != null)
                .filter(entry -> entry.getLanguage() != null)
                .filter(entry -> "en".equalsIgnoreCase(entry.getLanguage().getName()))
                .map(TextEntryRestDto::getFlavorText)
                .filter(text -> text != null && !text.isBlank())
                .map(text -> normalizeText(text))
                .findFirst()
                .orElse(null);
    }

    private static String normalizeText(String text) {
        if (text == null)
            return null;

        return text.replaceAll("\\s+", " ")
                .replace("\n", " ")
                .replace("\f", " ")
                .replace("\r", " ")
                .trim();
    }
}
