package com.pokemon.services.ability;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.pokemon.dtos.pokemon.ability.AbilityFilterDto;
import com.pokemon.dtos.pokemon.ability.ReadAbilityDto;
import com.pokemon.entities.Ability;
import com.pokemon.repositories.AbilityRepository;
import com.pokemon.services.service.AbstractQueryService;
import com.pokemon.services.service.SpecificationBuilder;
import com.pokemon.utils.enums.SearchOperation;
import com.pokemon.utils.mappers.ability.AbilityMapper;

import jakarta.validation.constraints.NotNull;

@Service
public class AbilityQueryService
        extends AbstractQueryService<Ability, Long, AbilityRepository, ReadAbilityDto, AbilityMapper> {

    public AbilityQueryService(AbilityRepository repository, AbilityMapper mapper) {
        super(mapper, repository);
    }

    public Page<ReadAbilityDto> filterAbilities(AbilityFilterDto filter, @NotNull Pageable pageable) {
        Specification<Ability> specification = buildSpecification(filter);

        final Pageable validatedPageable = pageable != null ? pageable : Pageable.ofSize(20);

        Page<Ability> abilityPage = repository.findAll(specification, validatedPageable);

        return abilityPage.map(mapper::toDto);
    }

    private Specification<Ability> buildSpecification(AbilityFilterDto filter) {
        SpecificationBuilder<Ability> specBuilder = new SpecificationBuilder<>();

        if (filter.getId() != null) {
            specBuilder.with("id", filter.getId(), null, SearchOperation.EQUAL);
        }

        if (filter.getName() != null && !filter.getName().isBlank()) {
            specBuilder.with("name", filter.getName(), null, SearchOperation.LIKE);
        }

        if (filter.getNameExact() != null && !filter.getNameExact().isBlank()) {
            specBuilder.with("name", filter.getNameExact(), null, SearchOperation.EQUAL);
        }

        return specBuilder.build();
    }

    @Override
    protected Class<Ability> getEntityClass() {
        return Ability.class;
    }

}
