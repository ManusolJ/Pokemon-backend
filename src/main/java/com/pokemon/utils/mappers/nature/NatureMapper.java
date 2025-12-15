package com.pokemon.utils.mappers.nature;

import org.mapstruct.Mapper;

import com.pokemon.dtos.pokemon.nature.ReadNatureDto;
import com.pokemon.entities.Nature;
import com.pokemon.utils.mappers.mapper.BaseMapper;
import com.pokemon.utils.mappers.mapper.BaseMapperConfig;

@Mapper(componentModel = "spring", config = BaseMapperConfig.class)
public interface NatureMapper extends BaseMapper<Nature, ReadNatureDto> {

}
