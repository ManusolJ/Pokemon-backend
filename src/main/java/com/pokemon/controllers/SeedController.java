package com.pokemon.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pokemon.services.service.SeedService;

import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/seed")
@RequiredArgsConstructor
public class SeedController {

    private final SeedService seedService;

    @PostMapping
    public ResponseEntity<Void> seedAllData() {
        seedService.seedAllData();
        return ResponseEntity.ok().build();
    }
}
