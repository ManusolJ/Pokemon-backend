package com.pokemon.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Represents a species of pokemon with various attributes.
 */
@Data
@Entity
@Table(name = "pokemon_species")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PokemonSpecies {

    /**
     * The unique identifier for the pokemon species.
     */
    @Id
    @EqualsAndHashCode.Include
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    /**
     * The name of the pokemon species.
     */
    @NotBlank
    @Size(max = 50)
    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    /**
     * The generation number the pokemon species was introduced in.
     */
    @Min(1)
    @Column(name = "generation")
    private Byte generation = null;

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
    private Byte genderRate = null;

    /**
     * The base capture rate for the pokemon species.
     */
    @Column(name = "capture_rate")
    private Short captureRate;

    /**
     * The base happiness level for the pokemon species.
     */
    @Column(name = "base_happiness")
    private Short baseHappiness;

    /**
     * The pokemon species this species evolves from, if any.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "previous_evolution")
    private PokemonSpecies previousEvolution = null;

    /**
     * A brief description or flavor text for the pokemon species.
     */
    @Column(name = "flavor_text", columnDefinition = "TEXT")
    private String flavorText;
}
