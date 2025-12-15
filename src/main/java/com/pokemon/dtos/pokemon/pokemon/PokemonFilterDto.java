package com.pokemon.dtos.pokemon.pokemon;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class PokemonFilterDto {

    private Long id;

    private Long species;

    private String name;

    private String nameExact;

    private String formName;

    private Boolean isDefault;

    private Long primaryType;

    private Long secondaryType;

    private BigDecimal heightMin;

    private BigDecimal heightMax;

    private BigDecimal weightMin;

    private BigDecimal weightMax;

    private Integer minHp;

    private Integer maxHp;

    private Integer minAttack;

    private Integer maxAttack;

    private Integer minDefense;

    private Integer maxDefense;

    private Integer minSpecialAttack;

    private Integer maxSpecialAttack;

    private Integer minSpecialDefense;

    private Integer maxSpecialDefense;

    private Integer minSpeed;

    private Integer maxSpeed;

    private Long abilityId;
}
