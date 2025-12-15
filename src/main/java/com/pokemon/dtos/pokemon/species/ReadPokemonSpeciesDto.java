package com.pokemon.dtos.pokemon.species;

import lombok.Data;

@Data
public class ReadPokemonSpeciesDto {

    private long id;

    private String name;

    private Integer generation;

    private boolean isBaby;

    private boolean isLegendary;

    private boolean isMythical;

    private Integer genderRate;

    private Integer captureRate;

    private Integer baseHappiness;

    private String description;

    private ReducedReadPokemonSpeciesDto previousEvolution;
}
