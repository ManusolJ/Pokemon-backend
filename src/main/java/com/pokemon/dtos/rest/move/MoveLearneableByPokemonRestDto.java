package com.pokemon.dtos.rest.move;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pokemon.dtos.rest.resource.PokeApiResource;

import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MoveLearneableByPokemonRestDto {

    @JsonProperty("move")
    private PokeApiResource move;
}
