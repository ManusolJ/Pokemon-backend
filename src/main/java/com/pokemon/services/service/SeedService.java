package com.pokemon.services.service;

import org.springframework.stereotype.Service;

import com.pokemon.services.ability.AbilityCommandService;
import com.pokemon.services.item.ItemCommandService;
import com.pokemon.services.move.MoveCommandService;
import com.pokemon.services.nature.NatureCommandService;
import com.pokemon.services.pokemon.PokemonCommandService;
import com.pokemon.services.species.PokemonSpeciesCommandService;
import com.pokemon.services.type.TypeCommandService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SeedService {

    private final IdentityMapService cacheService;
    private final TypeCommandService typeCommandService;
    private final MoveCommandService moveCommandService;
    private final ItemCommandService itemCommandService;
    private final NatureCommandService natureCommandService;
    private final AbilityCommandService abilityCommandService;
    private final PokemonCommandService pokemonCommandService;
    private final PokemonSpeciesCommandService pokemonSpeciesCommandService;

    public void seedAllData() {
        typeCommandService.fetchAndSave();
        pokemonSpeciesCommandService.fetchAndSave();
        moveCommandService.fetchAndSave();
        abilityCommandService.fetchAndSave();
        pokemonCommandService.fetchAndSave();
        natureCommandService.fetchAndSave();
        itemCommandService.fetchAndSave();
        cacheService.clearAll();
    }
}
