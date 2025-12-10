package com.pokemon.utils.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.pokemon.dtos.user.CreateUserDto;
import com.pokemon.dtos.user.ReadUserDto;
import com.pokemon.dtos.user.UpdateUserDto;
import com.pokemon.entities.User;
import com.pokemon.utils.mappers.mapper.BaseMapper;
import com.pokemon.utils.mappers.mapper.BaseMapperConfig;

@Mapper(componentModel = "spring", config = BaseMapperConfig.class)
public interface UserMapper extends BaseMapper<User, ReadUserDto> {

    @Override
    ReadUserDto toDto(User entity);

    @Mapping(source = "password", target = "passwordHash")
    User toEntity(CreateUserDto dto);

    void updateFromDto(UpdateUserDto dto, @MappingTarget User entity);
}
