package com.pokemon.utils.mappers.move;

import org.mapstruct.Mapper;

import com.pokemon.dtos.pokemon.move.ReadMoveDto;
import com.pokemon.entities.Move;
import com.pokemon.utils.mappers.mapper.BaseMapper;
import com.pokemon.utils.mappers.mapper.BaseMapperConfig;

@Mapper(componentModel = "spring", config = BaseMapperConfig.class)
public interface MoveMapper extends BaseMapper<Move, ReadMoveDto> {
    ReadMoveDto toDto(Move entity);
}
