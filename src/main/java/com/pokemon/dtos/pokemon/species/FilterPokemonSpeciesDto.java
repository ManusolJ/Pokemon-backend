package com.pokemon.dtos.pokemon.species;

import lombok.Data;

@Data
public class FilterPokemonSpeciesDto {

    private Long id;

    private String name;

    private String nameExact;

    private Integer generation;

    private Boolean isBaby;

    private Boolean isLegendary;

    private Boolean isMythical;

    private Integer genderRate;

    private Integer captureRate;

    private Integer baseHappiness;
}
