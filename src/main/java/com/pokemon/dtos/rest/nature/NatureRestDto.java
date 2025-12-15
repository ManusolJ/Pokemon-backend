package com.pokemon.dtos.rest.nature;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pokemon.dtos.rest.resource.PokeApiResource;

import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NatureRestDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("decreased_stat")
    private PokeApiResource decreasedStat;

    @JsonProperty("increased_stat")
    private PokeApiResource increasedStat;
}
