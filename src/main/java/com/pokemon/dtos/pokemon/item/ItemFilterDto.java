package com.pokemon.dtos.pokemon.item;

import lombok.Data;

@Data
public class ItemFilterDto {

    private Long id;

    private String name;

    private String nameExact;
}
