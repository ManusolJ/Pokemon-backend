package com.pokemon.dtos.pokemon.move;

import com.pokemon.dtos.pokemon.type.ReadTypeDto;
import com.pokemon.utils.enums.DamageClass;

import lombok.Data;

@Data
public class ReadMoveDto {

    private Long id;

    private String name;

    private ReadTypeDto type;

    private Integer accuracy;

    private Integer power;

    private Integer pp;

    private Integer priority;

    private DamageClass damageClass;

    private Integer effectChance;

    private String shortEffect;

    private String effect;
}
