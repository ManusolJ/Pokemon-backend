package com.pokemon.entities.CompositeIDs;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Composite key class for TeamMember entity.
 */
@Embeddable
@Data
@EqualsAndHashCode
public class TeamMemberId {

    /**
     * The unique identifier for the team.
     */
    @Column(name = "team_id", nullable = false)
    private Long teamId;

    /**
     * The unique identifier for the pokemon.
     */
    @Column(name = "user_pokemon_id", nullable = false)
    private Long userPokemonId;
}
