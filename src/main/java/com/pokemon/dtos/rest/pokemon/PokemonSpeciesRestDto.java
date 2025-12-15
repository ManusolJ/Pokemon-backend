package com.pokemon.dtos.rest.pokemon;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pokemon.dtos.rest.resource.FlavorTextRestDto;
import com.pokemon.dtos.rest.resource.PokeApiResource;

import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PokemonSpeciesRestDto {

    @JsonProperty("id")
    private long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("generation")
    private PokeApiResource generation;

    @JsonProperty("is_baby")
    private boolean isBaby;

    @JsonProperty("is_legendary")
    private boolean isLegendary;

    @JsonProperty("is_mythical")
    private boolean isMythical;

    @JsonProperty("gender_rate")
    private Integer genderRate;

    @JsonProperty("capture_rate")
    private Integer captureRate;

    @JsonProperty("base_happiness")
    private Integer baseHappiness;

    @JsonProperty("evolves_from_species")
    private PokeApiResource previousEvolution;

    @JsonProperty("flavor_text_entries")
    private List<FlavorTextRestDto> flavorTextEntries;
}
