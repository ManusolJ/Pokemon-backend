package com.pokemon.services.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import com.pokemon.dtos.rest.resource.PokeApiResource;
import com.pokemon.dtos.rest.resource.PokeApiResourceListDto;
import com.pokemon.repositories.BaseRepository;
import com.pokemon.utils.enums.CacheKey;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractCommandService<E, ID, R extends BaseRepository<E, ID>, A, M>
        implements BaseCommandService<E, ID> {

    protected final M apiMapper;
    protected final R repository;
    protected final RestClient restClient;
    protected final IdentityMapService cacheService;

    protected abstract String getEntityName();

    protected abstract CacheKey getCacheKey();

    protected abstract String getResourcePath();

    protected abstract Class<A> getApiDtoClass();

    protected abstract String extractEntityName(E entity);

    protected abstract Function<A, E> getEntityConverter();

    public void fetchAndSave() {
        List<PokeApiResource> resources = fetchResourceList();
        int initialFetchSize = resources.size();

        log.info("Fetched {} {} resources from PokéAPI", resources.size(), getEntityName());

        List<E> entities = resources.stream()
                .filter(Objects::nonNull)
                .map(this::fetchApiDto)
                .filter(Objects::nonNull)
                .map(getEntityConverter())
                .filter(Objects::nonNull)
                .toList();

        saveAllAndLog(entities, initialFetchSize);
    }

    protected List<PokeApiResource> fetchResourceList() {
        List<PokeApiResource> allResources = new ArrayList<>();

        String nextUrl = "";

        int page = 1;

        int totalPages;

        do {
            String uri = nextUrl.isBlank()
                    ? getResourcePath()
                    : extractRelativePath(nextUrl);

            if (uri.isBlank()) {
                throw new IllegalStateException("Resource path is blank for " + getEntityName());
            }

            PokeApiResourceListDto pageResult = restClient.get()
                    .uri(uri)
                    .retrieve()
                    .body(PokeApiResourceListDto.class);

            if (pageResult == null || pageResult.getResults() == null) {
                throw new IllegalStateException("Failed to fetch resource list for " + getEntityName() + " at " + uri);
            }

            totalPages = pageResult.getCount() / pageResult.getResults().size();

            log.debug("Fetching page {} of {} from entity {}", page++, totalPages, getEntityName());

            allResources.addAll(pageResult.getResults());
            nextUrl = pageResult.getNext();

        } while (nextUrl != null);

        return allResources;
    }

    protected A fetchApiDto(PokeApiResource resource) {
        final Class<A> apiDtoClass = getApiDtoClass();

        if (apiDtoClass == null) {
            throw new IllegalStateException("API DTO class is null for " + getEntityName());
        }

        if (resource.getUrl() == null || resource.getUrl().isBlank()) {
            log.warn("Resource URL is null or blank for {}", resource.getName());
            return null;
        }

        URI uri = URI.create(resource.getUrl());
        String path = extractRelativePath(uri.toString());

        if (path.isBlank()) {
            return null;
        }

        return restClient.get()
                .uri(path)
                .retrieve()
                .body(apiDtoClass);
    }

    @Transactional
    public void saveAllAndLog(List<E> entities, int initialFetchSize) {
        if (entities.isEmpty()) {
            throw new IllegalStateException(
                    "No " + getEntityName() + " were fetched and mapped from PokéAPI");
        }

        List<E> savedEntities = repository.saveAll(entities);

        if (!cacheService.cacheExists(getCacheKey())) {
            populateCache(savedEntities);
        }

        log.info("Fetched and saved {} {} from PokéAPI", savedEntities.size(), getEntityName());

        if (initialFetchSize != savedEntities.size()) {
            log.warn("Mismatch in fetched {}: initial fetch size was {}, but saved size is {}",
                    getEntityName(), initialFetchSize, savedEntities.size());
        }
    }

    protected void populateCache(List<E> entities) {
        int cachedCount = 0;
        for (E entity : entities) {
            String name = extractEntityName(entity);

            if (name != null) {
                cacheService.getOrCreate(
                        getCacheKey(),
                        name,
                        () -> entity);
                cachedCount++;
            }
        }
        log.debug("Cached {} {} name-to-ID mappings", cachedCount, getEntityName());
    }

    private String extractRelativePath(String absoluteUrl) {
        return absoluteUrl.replaceFirst("https://pokeapi.co/api/v2/", "");
    }
}
