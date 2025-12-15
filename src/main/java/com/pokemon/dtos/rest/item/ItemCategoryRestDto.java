package com.pokemon.dtos.rest.item;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pokemon.dtos.rest.resource.PokeApiResource;

import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemCategoryRestDto {

    @JsonProperty("items")
    private List<PokeApiResource> items;
}
