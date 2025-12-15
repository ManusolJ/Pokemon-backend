package com.pokemon.entities;

import com.pokemon.entities.CompositeIDs.TeamMemberId;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Represents a member of a Pokemon team, linking a team to a specific
 * user-owned Pokemon and its position within the team.
 */
@Data
@Entity
@Table(name = "team_members")
public class TeamMember {

    /**
     * Composite primary key consisting of teamId and userPokemonId.
     */
    @EmbeddedId
    private TeamMemberId id = new TeamMemberId();

    /**
     * The position of the Pokemon within the team (1 to 6).
     */
    @Column(name = "position", nullable = false)
    @NotNull
    @Min(1)
    @Max(6)
    private short position;

    @MapsId("teamId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @MapsId("userPokemonId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_pokemon_id", nullable = false)
    private UserPokemon userPokemon;
}
