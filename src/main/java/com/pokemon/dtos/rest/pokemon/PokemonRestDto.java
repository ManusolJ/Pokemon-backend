package com.pokemon.dtos.rest.pokemon;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pokemon.dtos.rest.ability.PokemonAbilityRestDto;
import com.pokemon.dtos.rest.move.MoveLearneableByPokemonRestDto;
import com.pokemon.dtos.rest.resource.PokeApiResource;
import com.pokemon.dtos.rest.types.TypeRestResourceDto;

import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PokemonRestDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("species")
    private PokeApiResource species;

    @JsonProperty("form_name")
    private PokeApiResource formName;

    @JsonProperty("order")
    private int order;

    @JsonProperty("height")
    private int height;

    @JsonProperty("weight")
    private int weight;

    @JsonProperty("is_default")
    private boolean isDefault;

    @JsonProperty("stats")
    private List<StatRestDto> stats;

    @JsonProperty("types")
    private List<TypeRestResourceDto> types;

    @JsonProperty("moves")
    private List<MoveLearneableByPokemonRestDto> moves;

    @JsonProperty("abilities")
    private List<PokemonAbilityRestDto> abilities;
}
