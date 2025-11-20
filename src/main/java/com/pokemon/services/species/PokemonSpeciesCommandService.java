package com.pokemon.services.species;

import java.net.URI;
import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.pokemon.dtos.pokemon.species.FilterPokemonSpeciesDto;
import com.pokemon.dtos.pokemon.species.ReadPokemonSpeciesDto;
import com.pokemon.dtos.rest.PokemonSpeciesRestDto;
import com.pokemon.dtos.rest.TextEntryRestDto;
import com.pokemon.dtos.rest.resource.PokeApiResource;
import com.pokemon.dtos.rest.resource.PokeApiResourceListDto;
import com.pokemon.entities.PokemonSpecies;
import com.pokemon.repositories.PokemonSpeciesRepository;
import com.pokemon.utils.mappers.PokemonSpeciesMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PokemonSpeciesCommandService {

    private final RestClient restClient;
    private final PokemonSpeciesMapper pokemonSpeciesMapper;
    private final PokemonSpeciesRepository pokemonSpeciesRepository;
    private final PokemonSpeciesQueryService pokemonSpeciesQueryService;

    public void fetchAndSavePokemonSpecies() {
        PokeApiResourceListDto resourceList = restClient.get()
                .uri("/pokemon-species?limit=1000")
                .retrieve()
                .body(PokeApiResourceListDto.class);

        if (resourceList == null || resourceList.getResults() == null) {
            throw new IllegalStateException("Failed to fetch Pokémon species from PokéAPI");
        }

        int initialFetchSize = resourceList.getResults().size();

        List<PokemonSpecies> entityList = resourceList.getResults().stream()
                .filter(Objects::nonNull)
                .map(this::fetchAndMap)
                .toList();

        if (entityList.isEmpty()) {
            throw new IllegalStateException("No Pokémon species were fetched and mapped from PokéAPI");
        }

        pokemonSpeciesRepository.saveAll(entityList);

        log.info("Fetched and saved {} Pokémon species from PokéAPI", entityList.size());
        if (initialFetchSize != entityList.size()) {
            log.warn("Mismatch in fetched Pokémon species: initial fetch size was {}, but saved size is {}",
                    initialFetchSize, entityList.size());
        }
    }

    private PokemonSpecies fetchAndMap(@NonNull PokeApiResource resource) {
        if (resource.getUrl() == null || resource.getUrl().isBlank()) {
            return null;
        }

        URI uri = URI.create(resource.getUrl());
        String path = uri.getPath().replaceFirst("/api/v2", "");

        if (path.isBlank()) {
            return null;
        }

        PokemonSpeciesRestDto dto = restClient.get()
                .uri(path)
                .retrieve()
                .body(PokemonSpeciesRestDto.class);

        if (dto == null) {
            return null;
        }

        String flavorText = fetchFlavorText(dto.getFlavorTextEntries());

        long evolvesFromId = fetchEvolvesFromId(dto.getEvolvesFromId());

        int generation = fetchGeneration(dto.getGeneration());

        return pokemonSpeciesRepository.findById(dto.getId())
                .orElseGet(() -> pokemonSpeciesMapper.toEntity(dto, flavorText, evolvesFromId, generation));
    }

    private String fetchFlavorText(List<TextEntryRestDto> entries) {
        return entries.stream()
                .filter(Objects::nonNull)
                .filter(entry -> "en".equalsIgnoreCase(entry.getLanguage().getName()))
                .map(TextEntryRestDto::getFlavorText)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse("No description available.");
    }

    private Long fetchEvolvesFromId(PokeApiResource evolvesFromResource) {
        if (evolvesFromResource == null || evolvesFromResource.getUrl() == null
                || evolvesFromResource.getUrl().isBlank()) {
            return null;
        }

        String speciesName = evolvesFromResource.getName();

        FilterPokemonSpeciesDto filterDto = new FilterPokemonSpeciesDto();
        filterDto.setName(speciesName);

        return pokemonSpeciesQueryService
                .filterPokemonSpecies(filterDto, Pageable.ofSize(1))
                .stream()
                .findFirst()
                .map(ReadPokemonSpeciesDto::getId)
                .orElse(null);

    }

    private int fetchGeneration(PokeApiResource generationResource) {
        if (generationResource == null || generationResource.getUrl() == null
                || generationResource.getUrl().isBlank()) {
            return 0;
        }

        String url = generationResource.getUrl();
        try {
            URI uri = URI.create(url);
            String path = uri.getPath();
            String[] segments = path.split("/");
            for (int i = segments.length - 1; i >= 0; i--) {
                if (!segments[i].isBlank()) {
                    return Integer.parseInt(segments[i]);
                }
            }
        } catch (Exception e) {
            log.warn("Failed to parse generation id from URL: {}", url, e);
        }
        return 0;
    }

}
