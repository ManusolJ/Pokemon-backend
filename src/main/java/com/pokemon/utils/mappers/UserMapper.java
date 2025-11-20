package com.pokemon.utils.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.pokemon.dtos.user.CreateUserDto;
import com.pokemon.dtos.user.ReadUserDto;
import com.pokemon.dtos.user.UpdateUserDto;
import com.pokemon.entities.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper extends BaseMapper<User, ReadUserDto> {

    ReadUserDto toDto(User entity);

    @Mapping(source = "password", target = "passwordHash")
    User toEntity(CreateUserDto dto);

    void updateFromDto(UpdateUserDto dto, @MappingTarget User entity);
}
