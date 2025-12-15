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

import com.pokemon.dtos.pokemon.ability.AbilityFilterDto;
import com.pokemon.dtos.pokemon.ability.ReadAbilityDto;
import com.pokemon.services.ability.AbilityQueryService;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/abilities")
public class AbilityController {

    private final AbilityQueryService queryService;

    @PostMapping("/filter")
    public ResponseEntity<Page<ReadAbilityDto>> filterAbilities(@RequestBody AbilityFilterDto filterDto,
            @PageableDefault(size = 20, sort = "id", direction = Direction.ASC) Pageable pageable) {
        final Pageable validatedPageable = pageable != null ? pageable : Pageable.ofSize(20);
        Page<ReadAbilityDto> abilities = queryService.filterAbilities(filterDto, validatedPageable);
        return ResponseEntity.ok(abilities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadAbilityDto> getAbilityById(@NotNull @PathVariable Long id) {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().build();
        }

        ReadAbilityDto abilityDto = queryService.findById(id);

        return ResponseEntity.ok(abilityDto);
    }
}
