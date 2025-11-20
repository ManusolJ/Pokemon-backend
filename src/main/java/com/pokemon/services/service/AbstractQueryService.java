package com.pokemon.services.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import com.pokemon.repositories.BaseRepository;
import com.pokemon.utils.mappers.BaseMapper;

import lombok.RequiredArgsConstructor;

@Validated
@Transactional(readOnly = true)
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

    public List<D> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .toList();
    }

    public boolean existsById(@NonNull ID id) {
        return repository.existsById(id);
    }

    public long count() {
        return repository.count();
    }
}
