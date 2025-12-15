package com.pokemon.dtos.pokemon.pokemon;

import com.pokemon.dtos.pokemon.species.ReducedReadPokemonSpeciesDto;
import com.pokemon.dtos.pokemon.type.ReadTypeDto;

import lombok.Data;

@Data
public class ReadPokemonDto {

    private Long id;

    private ReducedReadPokemonSpeciesDto species;

    private String name;

    private String formName;

    private Boolean isDefault;

    private Integer order;

    private ReadTypeDto primaryType;

    private ReadTypeDto secondaryType;

    private Double height;

    private Double weight;

    private Integer hp;

    private Integer attack;

    private Integer defense;

    private Integer specialAttack;

    private Integer specialDefense;

    private Integer speed;
}
