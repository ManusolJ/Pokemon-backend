package com.pokemon.utils.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.pokemon.dtos.user.CreateUserDto;
import com.pokemon.dtos.user.ReadUserDto;
import com.pokemon.dtos.user.UpdateUserDto;
import com.pokemon.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    ReadUserDto toReadUserDto(User user);

    List<ReadUserDto> toReadUserDtoList(List<User> users);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toEntity(CreateUserDto createUserDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(UpdateUserDto updateUserDto, @MappingTarget User user);
}
