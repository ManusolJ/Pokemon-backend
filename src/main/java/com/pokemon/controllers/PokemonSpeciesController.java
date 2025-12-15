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

import com.pokemon.dtos.pokemon.species.PokemonSpeciesFilterDto;
import com.pokemon.dtos.pokemon.species.ReadPokemonSpeciesDto;
import com.pokemon.services.species.PokemonSpeciesQueryService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/pokemon-species")
public class PokemonSpeciesController {

    private final PokemonSpeciesQueryService queryService;

    @PostMapping("/filter")
    public ResponseEntity<Page<ReadPokemonSpeciesDto>> filterPokemonSpecies(
            @Valid @RequestBody PokemonSpeciesFilterDto filter,
            @PageableDefault(size = 20, sort = "id", direction = Direction.ASC) Pageable pageable) {
        final Pageable validPageable = pageable == null ? Pageable.ofSize(20) : pageable;
        Page<ReadPokemonSpeciesDto> species = queryService.filterPokemonSpecies(filter, validPageable);
        return ResponseEntity.ok(species);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadPokemonSpeciesDto> getPokemonSpeciesByID(@NotNull @PathVariable Long id) {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().build();
        }

        ReadPokemonSpeciesDto species = queryService.findById(id);

        if (species == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(species);
        }
    }
}
