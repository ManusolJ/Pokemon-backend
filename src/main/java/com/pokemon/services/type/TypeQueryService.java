package com.pokemon.services.type;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.pokemon.dtos.pokemon.type.ReadTypeDto;
import com.pokemon.dtos.pokemon.type.TypeFilterDto;
import com.pokemon.entities.Type;
import com.pokemon.repositories.TypeRepository;
import com.pokemon.services.service.AbstractQueryService;
import com.pokemon.services.service.SpecificationBuilder;
import com.pokemon.utils.enums.SearchOperation;
import com.pokemon.utils.mappers.TypeMapper;

@Service
public class TypeQueryService extends AbstractQueryService<Type, Long, TypeRepository, ReadTypeDto, TypeMapper> {

    public TypeQueryService(TypeMapper mapper, TypeRepository repository) {
        super(mapper, repository, Type.class);
    }

    public Page<ReadTypeDto> filterTypes(TypeFilterDto filter, @NonNull Pageable pageable) {
        Specification<Type> specification = buildSpecification(filter);
        return repository.findAll(specification, pageable)
                .map(mapper::toDto);
    }

    private Specification<Type> buildSpecification(TypeFilterDto filter) {
        SpecificationBuilder<Type> specBuilder = new SpecificationBuilder<>();

        if (filter.getName() != null && !filter.getName().isBlank()) {
            specBuilder.with("name", filter.getName(), null, SearchOperation.LIKE);
        }

        if (filter.getNameExact() != null && !filter.getNameExact().isBlank()) {
            specBuilder.with("name", filter.getNameExact(), null, SearchOperation.EQUAL);
        }

        if (filter.getId() != null) {
            specBuilder.with("id", filter.getId(), null, SearchOperation.EQUAL);
        }

        return specBuilder.build();
    }
}
