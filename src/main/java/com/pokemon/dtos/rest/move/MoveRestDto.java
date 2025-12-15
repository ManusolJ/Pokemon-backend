package com.pokemon.dtos.rest.move;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pokemon.dtos.rest.resource.EffectTextRestDto;
import com.pokemon.dtos.rest.resource.FlavorTextRestDto;
import com.pokemon.dtos.rest.resource.PokeApiResource;
import com.pokemon.dtos.rest.types.TypeRestDto;

import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MoveRestDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("type")
    private TypeRestDto type;

    @JsonProperty("power")
    private Integer power;

    @JsonProperty("accuracy")
    private Integer accuracy;

    @JsonProperty("pp")
    private Integer pp;

    @JsonProperty("priority")
    private Integer priority;

    @JsonProperty("damage_class")
    private PokeApiResource damageClass;

    @JsonProperty("effect_chance")
    private Integer effectChance;

    @JsonProperty("effect_entries")
    private List<EffectTextRestDto> effectEntries;

    @JsonProperty("flavor_text_entries")
    private List<FlavorTextRestDto> flavorTextEntries;
}
