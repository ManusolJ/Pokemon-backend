package com.pokemon.dtos.pokemon.move;

import com.pokemon.utils.enums.DamageClass;

import lombok.Data;

@Data
public class MoveFilterDto {

    private Long id;

    private String name;

    private String nameExact;

    private Long typeId;

    private Integer minAccuracy;

    private Integer maxAccuracy;

    private Integer minPower;

    private Integer maxPower;

    private Integer minPp;

    private Integer maxPp;

    private Integer minPriority;

    private Integer maxPriority;

    private DamageClass damageClass;

    private Boolean hasPower;

    private Boolean hasAccuracy;

    private Boolean hasEffectChance;
}
