package com.pokemon.utils.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.pokemon.dtos.user.ReadUserDto;
import com.pokemon.dtos.user.UpdateUserDto;
import com.pokemon.entities.User;
import com.pokemon.utils.mappers.mapper.BaseMapper;
import com.pokemon.utils.mappers.mapper.BaseMapperConfig;

@Mapper(componentModel = "spring", config = BaseMapperConfig.class)
public interface UserMapper extends BaseMapper<User, ReadUserDto> {

    @Override
    ReadUserDto toDto(User entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateFromDto(UpdateUserDto dto, @MappingTarget User entity);
}
