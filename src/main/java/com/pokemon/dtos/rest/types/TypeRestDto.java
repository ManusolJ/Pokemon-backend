package com.pokemon.dtos.rest.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TypeRestDto {

    @JsonProperty("id")
    private long id;

    @JsonProperty("name")
    private String name;
}
