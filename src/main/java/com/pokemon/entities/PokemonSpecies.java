package com.pokemon.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Represents a species of pokemon with various attributes.
 */
@Entity
@Table(name = "pokemon_species")
@Data
public class PokemonSpecies {

    /**
     * The unique identifier for the pokemon species.
     */
    @Id
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private Long id;

    /**
     * The name of the pokemon species.
     */
    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    /**
     * The generation number the pokemon species was introduced in.
     */
    @Column(name = "generation")
    private Integer generation;

    /**
     * Indicates if the pokemon species is a baby pokemon.
     */
    @Column(name = "is_baby", nullable = false)
    private boolean isBaby = false;

    /**
     * Indicates if the pokemon species is legendary.
     */
    @Column(name = "is_legendary", nullable = false)
    private boolean isLegendary = false;

    /**
     * Indicates if the pokemon species is mythical.
     */
    @Column(name = "is_mythical", nullable = false)
    private boolean isMythical = false;

    /**
     * The ratio of male to female for the pokemon species. A value of -1 indicates
     * the species is genderless.
     */
    @Column(name = "gender_rate")
    private Integer genderRate = null;

    /**
     * The base capture rate for the pokemon species.
     */
    @Column(name = "capture_rate")
    private Integer captureRate;

    /**
     * The base happiness level for the pokemon species.
     */
    @Column(name = "base_happiness")
    private Integer baseHappiness;

    /**
     * The identifier of the pokemon species this species evolves from, if any.
     */
    @Column(name = "evolves_from_id")
    private Long evolvesFromId = null;

    /**
     * A brief description or flavor text for the pokemon species.
     */
    @Column(name = "description")
    private String description;
}
