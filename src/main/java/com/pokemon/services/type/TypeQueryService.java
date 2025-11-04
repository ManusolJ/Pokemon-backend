package com.pokemon.services.type;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.pokemon.dtos.pokemon.ReadTypeDto;
import com.pokemon.repositories.TypeRepository;
import com.pokemon.utils.mappers.TypeMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TypeQueryService {

    private final TypeRepository typeRepository;
    private final TypeMapper typeMapper;

    public ReadTypeDto getTypeById(long id) {
        return typeRepository.findById(id)
                .map(typeMapper::toReadDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        (String.format("Type with id %d not found", id))));
    }

    public ReadTypeDto getTypeByName(String name) {
        return typeRepository.findByNameIgnoreCase(name)
                .map(typeMapper::toReadDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("Type with name '%s' not found", name)));
    }

    public List<ReadTypeDto> getAllTypes() {
        return typeRepository.findAll().stream()
                .map(typeMapper::toReadDto)
                .toList();
    }
}
