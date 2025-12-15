package com.pokemon.dtos.pokemon.nature;

import lombok.Data;

@Data
public class NatureFilterDto {

    private String name;

    private String exactName;

    private String increasedStat;

    private String decreasedStat;
}
