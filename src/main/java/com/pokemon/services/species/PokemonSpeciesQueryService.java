package com.pokemon.services.species;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.pokemon.dtos.pokemon.species.FilterPokemonSpeciesDto;
import com.pokemon.dtos.pokemon.species.ReadPokemonSpeciesDto;
import com.pokemon.entities.PokemonSpecies;
import com.pokemon.repositories.PokemonSpeciesRepository;
import com.pokemon.services.service.AbstractQueryService;
import com.pokemon.services.service.SpecificationBuilder;
import com.pokemon.utils.enums.SearchOperation;
import com.pokemon.utils.mappers.PokemonSpeciesMapper;

@Service
@Validated
public class PokemonSpeciesQueryService extends
        AbstractQueryService<PokemonSpecies, Long, PokemonSpeciesRepository, ReadPokemonSpeciesDto, PokemonSpeciesMapper> {

    public PokemonSpeciesQueryService(PokemonSpeciesMapper mapper, PokemonSpeciesRepository repository) {
        super(mapper, repository, PokemonSpecies.class);
    }

    Page<ReadPokemonSpeciesDto> filterPokemonSpecies(FilterPokemonSpeciesDto filter, @NonNull Pageable pageable) {
        Specification<PokemonSpecies> specification = buildSpecification(filter);
        return repository.findAll(specification, pageable)
                .map(mapper::toDto);
    }

    private Specification<PokemonSpecies> buildSpecification(FilterPokemonSpeciesDto filter) {
        SpecificationBuilder<PokemonSpecies> specBuilder = new SpecificationBuilder<>();

        if (filter.getId() != null) {
            specBuilder.with("id", filter.getId(), null, SearchOperation.EQUAL);
        }

        if (filter.getName() != null && !filter.getName().isBlank()) {
            specBuilder.with("name", filter.getName(), null, SearchOperation.LIKE);
        }

        if (filter.getNameExact() != null && !filter.getNameExact().isBlank()) {
            specBuilder.with("name", filter.getNameExact(), null, SearchOperation.EQUAL);
        }

        if (filter.getGeneration() != null) {
            specBuilder.with("generation", filter.getGeneration(), null, SearchOperation.EQUAL);
        }

        if (filter.getIsBaby() != null) {
            specBuilder.with("isBaby", filter.getIsBaby(), null, SearchOperation.EQUAL);
        }

        if (filter.getIsLegendary() != null) {
            specBuilder.with("isLegendary", filter.getIsLegendary(), null, SearchOperation.EQUAL);
        }

        if (filter.getIsMythical() != null) {
            specBuilder.with("isMythical", filter.getIsMythical(), null, SearchOperation.EQUAL);
        }

        if (filter.getGenderRate() != null) {
            specBuilder.with("genderRate", filter.getGenderRate(), null, SearchOperation.EQUAL);
        }

        if (filter.getCaptureRate() != null) {
            specBuilder.with("captureRate", filter.getCaptureRate(), null, SearchOperation.EQUAL);
        }

        if (filter.getBaseHappiness() != null) {
            specBuilder.with("baseHappiness", filter.getBaseHappiness(), null, SearchOperation.EQUAL);
        }

        return specBuilder.build();
    }
}
