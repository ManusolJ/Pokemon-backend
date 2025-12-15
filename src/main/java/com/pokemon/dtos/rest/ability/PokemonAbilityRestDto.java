package com.pokemon.dtos.rest.ability;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pokemon.dtos.rest.resource.PokeApiResource;

import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PokemonAbilityRestDto {

    @JsonProperty("slot")
    byte slot;

    @JsonProperty("is_hidden")
    boolean isHidden;

    @JsonProperty("ability")
    PokeApiResource ability;
}
