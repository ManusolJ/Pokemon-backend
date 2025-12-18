package com.pokemon.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.pokemon.entities.User;

/**
 * Repository interface for {@link User} persistence operations.
 * 
 * Extends {@link BaseRepository} to inherit standard CRUD functionality and defines custom query
 * methods related to {@code User} state and identity.
 * 
 */
@Repository
public interface UserRepository extends BaseRepository<User, Long> {

    /**
     * Checks whether a {@link User} exists with the given username.
     *
     * @param username the username to check for existence; must not be {@code null}
     * @return {@code true} if a user with the given username exists, {@code false} otherwise
     */
    boolean existsByUsername(String username);

    /**
     * Updates the {@code active} status of all {@link User} entities whose identifiers are
     * contained in the given collection.
     * 
     * This operation is executed as a bulk update query. The persistence context is automatically
     * cleared and flushed to ensure consistency with the database.
     *
     * @param active the new active status to apply
     * @param ids the identifiers of the users to update; must not be {@code null}
     * @return the number of rows affected by the update
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE User u SET u.active = :active WHERE u.id IN :ids")
    int updateActiveStatusByIds(boolean active, Iterable<Long> ids);
}
