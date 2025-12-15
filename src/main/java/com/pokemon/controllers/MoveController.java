package com.pokemon.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pokemon.dtos.pokemon.move.MoveFilterDto;
import com.pokemon.dtos.pokemon.move.ReadMoveDto;
import com.pokemon.services.move.MoveQueryService;

import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/moves")
public class MoveController {

    private final MoveQueryService queryService;

    @PostMapping("/filter")
    public ResponseEntity<Page<ReadMoveDto>> filterMoves(@RequestBody MoveFilterDto filter,
            Pageable pageable) {
        final Pageable validPageable = pageable == null ? Pageable.ofSize(20)
                : pageable;
        Page<ReadMoveDto> moves = queryService.filterMoves(filter, validPageable);
        return ResponseEntity.ok(moves);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadMoveDto> getMoveById(@PathVariable Long id) {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().build();
        }

        ReadMoveDto move = queryService.findById(id);

        return ResponseEntity.ok(move);
    }
}
