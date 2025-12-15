package com.pokemon.dtos.pokemon.ability;

import lombok.Data;

@Data
public class AbilityFilterDto {

    private Long id;

    private String name;

    private String nameExact;
}
