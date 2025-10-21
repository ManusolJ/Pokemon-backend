package com.pokemon.entities;

import com.pokemon.entities.CompositeIDs.TeamMemberId;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Represents a member of a Pokemon team, linking a team to a specific
 * user-owned Pokemon and its position within the team.
 */
@Entity
@Table(name = "team_members")
@Data
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
}
