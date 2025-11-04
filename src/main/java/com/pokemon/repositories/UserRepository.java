package com.pokemon.repositories;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pokemon.entities.User;
import com.pokemon.utils.enums.UserRole;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByUsername(String username);

  Optional<User> findByUsernameIgnoreCase(String username);

  boolean existsByUsername(String username);

  boolean existsByUsernameIgnoreCase(String username);

  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query("UPDATE User u SET u.active = :active WHERE u.id IN :ids")
  int updateActiveStatusByIds(@Param("active") boolean active, @Param("ids") List<Long> ids);

  @Query("""
      SELECT u FROM User u
      WHERE (:username IS NULL OR LOWER(u.username) LIKE LOWER(CONCAT('%', :username, '%')))
        AND (:roles    IS NULL OR u.role IN :roles)
        AND (:active   IS NULL OR u.active = :active)
        AND (:createdFrom IS NULL OR u.createdAt >= :createdFrom)
        AND (:createdTo   IS NULL OR u.createdAt  < :createdTo)
        AND (:updatedFrom IS NULL OR u.updatedAt >= :updatedFrom)
        AND (:updatedTo   IS NULL OR u.updatedAt  < :updatedTo)
      """)
  Page<User> findUsers(
      @Param("username") String username,
      @Param("roles") Collection<UserRole> roles,
      @Param("active") Boolean active,
      @Param("createdFrom") Instant createdFrom,
      @Param("createdTo") Instant createdTo,
      @Param("updatedFrom") Instant updatedFrom,
      @Param("updatedTo") Instant updatedTo,
      Pageable pageable);

  long countByActiveTrue();

  long countByActiveTrueAndRole(UserRole role);
}
