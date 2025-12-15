package com.pokemon.utils.mappers.ability;

import org.mapstruct.Mapper;

import com.pokemon.dtos.pokemon.ability.ReadAbilityDto;
import com.pokemon.entities.Ability;
import com.pokemon.utils.mappers.mapper.BaseMapper;
import com.pokemon.utils.mappers.mapper.BaseMapperConfig;

@Mapper(componentModel = "spring", config = BaseMapperConfig.class)
public interface AbilityMapper extends BaseMapper<Ability, ReadAbilityDto> {
    ReadAbilityDto toDto(Ability ability);

}
