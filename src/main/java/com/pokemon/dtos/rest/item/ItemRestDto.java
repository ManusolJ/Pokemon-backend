package com.pokemon.dtos.rest.item;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pokemon.dtos.rest.resource.EffectTextRestDto;
import com.pokemon.dtos.rest.resource.FlavorTextRestDto;

import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemRestDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("effect_entries")
    private List<EffectTextRestDto> effectEntries;

    @JsonProperty("flavor_text_entries")
    private List<FlavorTextRestDto> flavorTextEntries;
}
