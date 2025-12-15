package com.pokemon.dtos.rest.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class EffectTextRestDto {

    @JsonProperty("effect")
    private String effect;

    @JsonProperty("short_effect")
    private String shortEffect;

    @JsonProperty("language")
    private PokeApiResource language;
}
