package com.pokemon.services.nature;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.pokemon.dtos.pokemon.nature.NatureFilterDto;
import com.pokemon.dtos.pokemon.nature.ReadNatureDto;
import com.pokemon.entities.Nature;
import com.pokemon.repositories.NatureRepository;
import com.pokemon.services.service.AbstractQueryService;
import com.pokemon.services.service.SpecificationBuilder;
import com.pokemon.utils.enums.SearchOperation;
import com.pokemon.utils.mappers.nature.NatureMapper;

@Service
public class NatureQueryService
        extends AbstractQueryService<Nature, Long, NatureRepository, ReadNatureDto, NatureMapper> {

    public NatureQueryService(NatureMapper mapper, NatureRepository repository) {
        super(mapper, repository);
    }

    public Page<ReadNatureDto> filterNatures(NatureFilterDto filter, Pageable pageable) {
        Specification<Nature> specification = buildSpecification(filter);

        final Pageable validPageable = pageable != null ? pageable : Pageable.ofSize(20);

        Page<Nature> naturePage = repository.findAll(specification, validPageable);

        return naturePage.map(mapper::toDto);
    }

    private Specification<Nature> buildSpecification(NatureFilterDto filter) {
        SpecificationBuilder<Nature> specBuilder = new SpecificationBuilder<>();

        if (filter.getName() != null && !filter.getName().isBlank()) {
            specBuilder.with("name", filter.getName(), null, SearchOperation.LIKE);
        }

        if (filter.getExactName() != null && !filter.getExactName().isBlank()) {
            specBuilder.with("name", filter.getExactName(), null, SearchOperation.EQUAL);
        }

        if (filter.getIncreasedStat() != null && !filter.getIncreasedStat().isBlank()) {
            specBuilder.with("increasedStat", filter.getIncreasedStat(), null, SearchOperation.EQUAL);
        }

        if (filter.getDecreasedStat() != null && !filter.getDecreasedStat().isBlank()) {
            specBuilder.with("decreasedStat", filter.getDecreasedStat(), null, SearchOperation.EQUAL);
        }

        if (filter.getIncreasedStat() != null && filter.getDecreasedStat() != null &&
                filter.getIncreasedStat().equals(filter.getDecreasedStat())) {
            specBuilder.with("increasedStat", filter.getIncreasedStat(), null, SearchOperation.EQUAL);
            specBuilder.with("decreasedStat", filter.getDecreasedStat(), null, SearchOperation.EQUAL);
        }

        return specBuilder.build();
    }

    @Override
    protected Class<Nature> getEntityClass() {
        return Nature.class;
    }

}
