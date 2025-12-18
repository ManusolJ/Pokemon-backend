package com.pokemon.services.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import com.pokemon.repositories.BaseRepository;
import com.pokemon.utils.mappers.mapper.MapperToDto;
import lombok.RequiredArgsConstructor;

/**
 * Abstract base service providing read-only query operations for entities.
 * <p>
 * This class encapsulates common lookup logic, including entity retrieval by identifier and mapping
 * to DTOs. It is intended to be extended by concrete query services that define entity-specific
 * metadata.
 * </p>
 *
 * <p>
 * All operations are executed within a read-only transactional context to ensure consistency while
 * preventing unintended modifications.
 * </p>
 *
 * @param <E> the entity type
 * @param <ID> the entity identifier type
 * @param <R> the repository type
 * @param <D> the DTO type returned to callers
 * @param <M> the mapper type used to convert entities to DTOs
 */
@Validated
@RequiredArgsConstructor
@Transactional(readOnly = true)
public abstract class AbstractQueryService<E, ID, R extends BaseRepository<E, ID>, D, M extends MapperToDto<E, D>, F> {

    /**
     * Mapper used to convert entities into DTO representations.
     */
    protected final M mapper;

    /**
     * Repository used to perform persistence queries.
     */
    protected final R repository;

    /**
     * Retrieves an entity by its identifier and maps it to a DTO.
     *
     * @param id the identifier of the entity to retrieve; must not be {@code null}
     * @return the mapped DTO representation of the entity
     * @throws IllegalArgumentException if no entity with the given identifier exists
     */
    public D findById(@NonNull ID id) {
        return repository.findById(id).map(mapper::toDto).orElseThrow(() -> new IllegalArgumentException(String.format("%s with id %s not found", getEntityClassName(), id)));
    }

    /**
     * Filters entities based on the provided criteria and returns a paginated list of DTOs.
     * 
     * @param filter
     * @param pageable
     * @return a page of DTOs matching the filter criteria
     */
    protected abstract Page<D> filterEntities(@NonNull F filter, @NonNull Pageable pageable);

    /**
     * Counts the number of entities matching the provided filter criteria.
     * 
     * @param filter
     * @return the count of matching entities
     */
    protected abstract Long countFilteredEntities(@NonNull F filter);

    /**
     * Returns a human-readable entity name used for error messages.
     *
     * @return the entity class name
     */
    protected abstract String getEntityClassName();
}

