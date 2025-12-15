package com.pokemon.dtos.pokemon.nature;

import lombok.Data;

@Data
public class ReadNatureDto {

    private Long id;

    private String name;

    private String decreasedStat;

    private String increasedStat;
}
