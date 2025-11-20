package com.pokemon.dtos.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pokemon.dtos.rest.resource.PokeApiResource;

import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class TextEntryRestDto {

    @JsonProperty("flavor_text")
    private String flavorText;

    @JsonProperty("language")
    private PokeApiResource language;
}
