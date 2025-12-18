package com.pokemon.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Base repository interface providing common persistence operations for all entities.
 * 
 * This interface is not intended to be instantiated directly. It serves as a shared parent for
 * concrete repository interfaces, exposing standard CRUD operations via {@link JpaRepository} and
 * criteria-based query support via {@link JpaSpecificationExecutor}.
 *
 * @param <T> the type of the entity
 * @param <ID> the type of the entity identifier
 */
@NoRepositoryBean
public interface BaseRepository<T, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

}

