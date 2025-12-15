package com.pokemon.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pokemon.dtos.pokemon.pokemon.PokemonFilterDto;
import com.pokemon.dtos.pokemon.pokemon.ReadPokemonDto;
import com.pokemon.services.pokemon.PokemonQueryService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/pokemon")
public class PokemonController {

    private final PokemonQueryService queryService;

    @PostMapping("/filter")
    public ResponseEntity<Page<ReadPokemonDto>> getPokemonSpecies(
            @Valid @RequestBody PokemonFilterDto filter,
            @PageableDefault(size = 20, sort = "id", direction = Direction.ASC) Pageable pageable) {
        final Pageable validPageable = pageable == null ? Pageable.ofSize(20) : pageable;
        Page<ReadPokemonDto> species = queryService.filterPokemon(filter, validPageable);
        return ResponseEntity.ok(species);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadPokemonDto> getPokemonSpeciesById(@NotNull @PathVariable Long id) {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().build();
        }

        ReadPokemonDto species = queryService.findById(id);

        if (species == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(species);
        }
    }
}
