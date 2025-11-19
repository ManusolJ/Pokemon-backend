package com.pokemon.services.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

public interface BaseQueryService<D, ID> {

    D findById(@NonNull ID id);

    Page<D> findAll(@NonNull Pageable pageable);

    boolean existsById(@NonNull ID id);

    long count();
}
