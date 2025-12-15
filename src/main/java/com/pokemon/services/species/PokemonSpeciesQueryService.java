package com.pokemon.services.species;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.pokemon.dtos.pokemon.species.PokemonSpeciesFilterDto;
import com.pokemon.dtos.pokemon.species.ReadPokemonSpeciesDto;
import com.pokemon.entities.PokemonSpecies;
import com.pokemon.repositories.PokemonSpeciesRepository;
import com.pokemon.services.service.AbstractQueryService;
import com.pokemon.services.service.SpecificationBuilder;
import com.pokemon.utils.enums.SearchOperation;
import com.pokemon.utils.mappers.species.PokemonSpeciesMapper;

import jakarta.persistence.criteria.JoinType;

@Service
@Validated
public class PokemonSpeciesQueryService extends
        AbstractQueryService<PokemonSpecies, Long, PokemonSpeciesRepository, ReadPokemonSpeciesDto, PokemonSpeciesMapper> {

    public PokemonSpeciesQueryService(PokemonSpeciesMapper mapper, PokemonSpeciesRepository repository) {
        super(mapper, repository);
    }

    public Page<ReadPokemonSpeciesDto> filterPokemonSpecies(PokemonSpeciesFilterDto filter,
            @NonNull Pageable pageable) {
        Specification<PokemonSpecies> specification = buildSpecification(filter);

        Specification<PokemonSpecies> fetchSpec = (root, query, cb) -> {
            if (query != null && !query.getResultType().equals(Long.class)) {
                root.join("previousEvolution", JoinType.LEFT);
                query.distinct(true);
            }
            return cb.conjunction();
        };

        Specification<PokemonSpecies> finalSpec = specification == null ? fetchSpec : specification.and(fetchSpec);

        Page<PokemonSpecies> pokemonSpeciesPage = repository.findAll(finalSpec, pageable);

        return pokemonSpeciesPage.map(mapper::toDto);
    }

    private Specification<PokemonSpecies> buildSpecification(PokemonSpeciesFilterDto filter) {
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

    @Override
    protected Class<PokemonSpecies> getEntityClass() {
        return PokemonSpecies.class;
    }
}
