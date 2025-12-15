package com.pokemon.dtos.rest.pokemon;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pokemon.dtos.rest.resource.PokeApiResource;

import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatRestDto {

    @JsonProperty("base_stat")
    private int baseStat;

    @JsonProperty("stat")
    private PokeApiResource stat;
}
