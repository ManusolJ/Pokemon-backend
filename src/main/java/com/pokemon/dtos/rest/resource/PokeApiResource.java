package com.pokemon.dtos.rest.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PokeApiResource {

    @JsonProperty("name")
    private String name;

    @JsonProperty("url")
    private String url;
}
