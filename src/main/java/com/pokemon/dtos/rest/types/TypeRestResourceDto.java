package com.pokemon.dtos.rest.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pokemon.dtos.rest.resource.PokeApiResource;

import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TypeRestResourceDto {

    @JsonProperty("slot")
    private int slot;

    @JsonProperty("type")
    private PokeApiResource type;
}
