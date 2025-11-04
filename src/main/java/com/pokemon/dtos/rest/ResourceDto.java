package com.pokemon.dtos.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResourceDto {

    @JsonProperty("name")
    private String name;

    @JsonProperty("url")
    private String url;
}
