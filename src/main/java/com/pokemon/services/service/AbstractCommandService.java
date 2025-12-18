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

/**
 * Abstract base service providing common command-style operations for fetching, mapping,
 * persisting, and caching entities obtained from the PokéAPI.
 * <p>
 * This class encapsulates the full lifecycle of:
 * <ul>
 * <li>Fetching paginated resources from the API</li>
 * <li>Retrieving detailed DTOs</li>
 * <li>Mapping DTOs to entities</li>
 * <li>Persisting entities in the database</li>
 * <li>Populating an identity-based cache</li>
 * </ul>
 * Concrete implementations must supply entity-specific details such as resource paths, mapping
 * logic, and cache configuration.
 * </p>
 *
 * @param <E> the entity type
 * @param <ID> the entity identifier type
 * @param <R> the repository type
 * @param <D> the API DTO type
 * @param <M> the mapper type used to convert DTOs to entities
 */
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractCommandService<E, ID, R extends BaseRepository<E, ID>, D, M> {

    /**
     * Mapper used to convert API DTOs into entities.
     */
    protected final M apiMapper;

    /**
     * Repository used for persistence operations.
     */
    protected final R repository;

    /**
     * REST client used to communicate with the external PokéAPI.
     */
    protected final RestClient restClient;

    /**
     * Identity-based cache service for name-to-entity lookups.
     */
    protected final IdentityMapService cacheService;

    /**
     * Returns the relative API resource path used to fetch the initial resource list.
     *
     * @return the resource path (e.g. /pokemon)
     */
    public abstract String getResourcePath();

    /**
     * Returns a human-readable entity name used for logging and error messages.
     *
     * @return the entity class name
     */
    public abstract String getEntityClassName();

    /**
     * Extracts the unique name field from the given entity instance.
     *
     * @param entity the entity instance
     * @return the extracted name, or {@code null} if not available
     */
    public abstract String extractNameFieldFromEntity(E entity);

    /**
     * Returns the cache key under which entities of this type are stored.
     *
     * @return the cache key
     */
    public abstract CacheKey getCacheKey();

    /**
     * Returns the API DTO class used when deserializing API responses.
     *
     * @return the API DTO class
     */
    public abstract Class<D> getApiDtoClass();

    /**
     * Returns a converter function used to transform API DTOs into entities.
     *
     * @return a DTO-to-entity conversion function
     */
    public abstract Function<D, E> getEntityConverter();

    /**
     * Fetches all resources from the API, retrieves their detailed representations, maps them to
     * entities, and persists them to the database.
     * <p>
     * This method coordinates the full fetch-and-save workflow and logs progress and consistency
     * checks.
     * </p>
     */
    public void fetchAndSave() {
        List<PokeApiResource> resources = fetchResourceList();

        int initialFetchSize = resources.size();

        log.info("Fetched {} {} resources from PokéAPI.", resources.size(), getEntityClassName());

        List<E> entities = resources.stream().filter(Objects::nonNull).map(this::fetchApiDto).filter(Objects::nonNull).map(getEntityConverter()).filter(Objects::nonNull).toList();

        saveAllAndLog(entities, initialFetchSize);
    }

    /**
     * Fetches the full list of API resources, handling pagination transparently.
     *
     * @return a list of API resources
     * @throws IllegalStateException if resource retrieval fails or configuration is invalid
     */
    protected List<PokeApiResource> fetchResourceList() {
        List<PokeApiResource> allResources = new ArrayList<>();

        String nextUrl = "";

        int page = 1;
        double totalPages = 0;

        do {
            String uri = getUrlPath(nextUrl);

            if (uri.isBlank()) {
                throw new IllegalStateException("Resource path is blank for " + getEntityClassName());
            }

            PokeApiResourceListDto pageResult = restClient.get().uri(uri).retrieve().body(PokeApiResourceListDto.class);

            if (pageResult == null || pageResult.getResults() == null) {
                throw new IllegalStateException("Failed to fetch resource list for " + getEntityClassName() + " at " + uri);
            }

            totalPages = Math.ceil(pageResult.getCount() / pageResult.getResults().size());

            log.debug("Fetching page {} of {} from entity {}", page++, totalPages, getEntityClassName());

            allResources.addAll(pageResult.getResults());
            nextUrl = pageResult.getNext();

        } while (nextUrl != null);

        return allResources;
    }

    /**
     * Fetches and deserializes the detailed API DTO for the given resource.
     *
     * @param resource the API resource descriptor
     * @return the deserialized API DTO
     * @throws IllegalStateException if the DTO class or resource URL is invalid
     */
    protected D fetchApiDto(PokeApiResource resource) {
        final Class<D> dto = getApiDtoClass();

        if (dto == null || resource == null) {
            throw new IllegalStateException("API DTO class is null for " + getEntityClassName());
        }

        if (resource.getUrl() == null || resource.getUrl().isBlank()) {
            throw new IllegalStateException("Resource URL is blank for " + resource.getName());
        }

        URI uri = URI.create(resource.getUrl());

        String path = transformToRelativePath(uri.toString());

        if (path.isBlank() || path == null) {
            throw new IllegalStateException("Transformed path is blank for URL: " + resource.getUrl());
        }

        return restClient.get().uri(path).retrieve().body(dto);
    }

    /**
     * Persists the given entities, populates the cache if necessary, and logs consistency
     * information.
     *
     * @param entities the entities to persist
     * @param initialFetchSize the number of resources originally fetched
     * @throws IllegalStateException if no entities are provided
     */
    @Transactional
    public void saveAllAndLog(List<E> entities, int initialFetchSize) {
        if (entities.isEmpty()) {
            throw new IllegalStateException("No " + getEntityClassName() + " entities were fetched and/or mapped from PokéAPI");
        }

        List<E> savedEntities = repository.saveAll(entities);

        if (!cacheService.cacheExists(getCacheKey())) {
            populateCache(savedEntities);
        }

        log.info("Fetched and saved {} {} from PokéAPI to DB.", savedEntities.size(), getEntityClassName());

        if (initialFetchSize != savedEntities.size()) {
            log.warn("Mismatch in fetched {}: initial fetch size was {}, but saved size is {}", getEntityClassName(), initialFetchSize, savedEntities.size());
        }
    }

    /**
     * Populates the identity cache using the provided entities.
     *
     * @param entities the entities to cache
     */
    protected void populateCache(List<E> entities) {
        int cachedCount = 0;

        for (E entity : entities) {
            String name = extractNameFieldFromEntity(entity);

            if (name != null) {
                cacheService.getOrCreate(getCacheKey(), name, () -> entity);
                cachedCount++;
            }
        }

        log.debug("Cached {} {} name-to-ID mappings", cachedCount, getEntityClassName());
    }

    /**
     * Converts an absolute PokéAPI URL into a relative API path.
     *
     * @param url the absolute URL
     * @return the relative API path
     */
    private String transformToRelativePath(String url) {
        return url.replaceFirst("https://pokeapi.co/api/v2/", "");
    }

    /**
     * Resolves the effective API path based on the provided URL.
     * <p>
     * If the URL is blank, the base resource path is returned.
     * </p>
     *
     * @param url the next-page URL
     * @return the resolved relative API path
     */
    private String getUrlPath(String url) {
        if (url == null || url.isBlank()) {
            return getResourcePath();
        } else {
            return transformToRelativePath(url);
        }
    }
}

