package com.pokemon.services.species;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.pokemon.dtos.rest.pokemon.PokemonSpeciesRestDto;
import com.pokemon.dtos.rest.resource.PokeApiResource;
import com.pokemon.entities.PokemonSpecies;
import com.pokemon.repositories.PokemonSpeciesRepository;
import com.pokemon.services.service.AbstractCommandService;
import com.pokemon.services.service.IdentityMapService;
import com.pokemon.utils.enums.CacheKey;
import com.pokemon.utils.mappers.species.PokemonSpeciesApiMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PokemonSpeciesCommandService extends
        AbstractCommandService<PokemonSpecies, Long, PokemonSpeciesRepository, PokemonSpeciesRestDto, PokemonSpeciesApiMapper> {

    public PokemonSpeciesCommandService(PokemonSpeciesRepository repository, PokemonSpeciesApiMapper mapper,
            RestClient restClient, IdentityMapService cacheService) {
        super(mapper, repository, restClient, cacheService);
    }

    @Override
    protected CacheKey getCacheKey() {
        return CacheKey.POKEMON_SPECIES;
    }

    @Override
    protected String extractEntityName(PokemonSpecies entity) {
        return entity.getName();
    }

    @Override
    protected String getResourcePath() {
        return "/pokemon-species?limit=20";
    }

    @Override
    protected Class<PokemonSpeciesRestDto> getApiDtoClass() {
        return PokemonSpeciesRestDto.class;
    }

    @Override
    protected String getEntityName() {
        return "species";
    }

    @Override
    protected Function<PokemonSpeciesRestDto, PokemonSpecies> getEntityConverter() {
        return null;
    }

    @Override
    public void fetchAndSave() {
        List<PokeApiResource> resources = fetchResourceList();
        int initialFetchSize = resources.size();

        List<PokemonSpecies> entities = new ArrayList<>();

        List<PokemonSpeciesRestDto> dtos = resources.stream()
                .map(this::fetchApiDto)
                .filter(Objects::nonNull)
                .toList();

        List<PokemonSpecies> firstPassEntities = dtos.stream()
                .map(apiMapper::toEntity)
                .map(entity -> cacheService.getOrCreate(getCacheKey(), entity.getName(), () -> entity))
                .filter(Objects::nonNull)
                .toList();

        if (firstPassEntities.isEmpty()) {
            throw new IllegalStateException(
                    "No " + getEntityName() + " were fetched and mapped from PokéAPI");
        }

        repository.saveAllAndFlush(firstPassEntities);

        for (PokemonSpeciesRestDto dto : dtos) {
            String name = dto.getName();
            PokemonSpecies species = cacheService.get(getCacheKey(), name);
            if (dto.getPreviousEvolution() != null) {
                String prevName = dto.getPreviousEvolution().getName();
                PokemonSpecies prevSpecies = cacheService.get(getCacheKey(), prevName);
                if (prevSpecies == null) {
                    log.warn("Previous evolution '{}' for '{}' not found", prevName, name);
                } else {
                    species.setPreviousEvolution(prevSpecies);
                }
                entities.add(species);
            }
        }

        entities = entities.stream().filter(Objects::nonNull).distinct().toList();

        saveAllAndLog(entities, initialFetchSize);
    }

    @Override
    public void saveAllAndLog(List<PokemonSpecies> entities, int initialFetchSize) {
        if (entities.isEmpty()) {
            throw new IllegalStateException(
                    "No " + getEntityName() + " were fetched and mapped from PokéAPI");
        }

        List<PokemonSpecies> savedEntities = repository.saveAll(entities);

        int savedCount = savedEntities.size();

        log.info("Fetched and saved {} {} from PokéAPI", savedEntities, getEntityName());

        if (initialFetchSize != savedCount) {
            log.warn("Mismatch in fetched {}: initial fetch size was {}, but saved size is {}",
                    getEntityName(), initialFetchSize, savedCount);
        }
    }

}
