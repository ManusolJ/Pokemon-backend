package com.pokemon.utils.mappers.item;

import org.mapstruct.Mapper;

import com.pokemon.dtos.pokemon.item.ReadItemDto;
import com.pokemon.entities.Item;
import com.pokemon.utils.mappers.mapper.BaseMapper;
import com.pokemon.utils.mappers.mapper.BaseMapperConfig;

@Mapper(componentModel = "spring", config = BaseMapperConfig.class)
public interface ItemMapper extends BaseMapper<Item, ReadItemDto> {

}
