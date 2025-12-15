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

import com.pokemon.dtos.pokemon.type.ReadTypeDto;
import com.pokemon.dtos.pokemon.type.TypeFilterDto;
import com.pokemon.services.type.TypeQueryService;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/types")
public class TypeController {

    private final TypeQueryService queryService;

    @PostMapping("/filter")
    public ResponseEntity<Page<ReadTypeDto>> getTypes(@RequestBody TypeFilterDto filter,
            @PageableDefault(size = 20, sort = "id", direction = Direction.ASC) Pageable pageable) {
        final Pageable validPageable = pageable == null ? Pageable.ofSize(20) : pageable;
        Page<ReadTypeDto> types = queryService.filterTypes(filter, validPageable);
        return ResponseEntity.ok(types);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadTypeDto> getTypeByID(@NotNull @PathVariable Long id) {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().build();
        }

        ReadTypeDto type = queryService.findById(id);

        if (type == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(type);
        }
    }
}
