package com.pokemon.services.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import com.pokemon.repositories.BaseRepository;
import com.pokemon.utils.mappers.BaseMapper;

import lombok.RequiredArgsConstructor;

@Service
@Validated
@RequiredArgsConstructor
public abstract class AbstractQueryService<E, ID, R extends BaseRepository<E, ID>, D, M extends BaseMapper<E, D>>
        implements BaseQueryService<D, ID> {

    protected final M mapper;
    protected final R repository;
    private final Class<E> entityClass;

    public D findById(@NonNull ID id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Entity %s with id %s not found", entityClass.getSimpleName(), id)));
    }

    public Page<D> findAll(@NonNull Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toDto);
    }

    public boolean existsById(@NonNull ID id) {
        return repository.existsById(id);
    }

    public long count() {
        return repository.count();
    }
}
