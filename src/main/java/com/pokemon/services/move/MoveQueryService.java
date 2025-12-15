package com.pokemon.services.move;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.pokemon.dtos.pokemon.move.MoveFilterDto;
import com.pokemon.dtos.pokemon.move.ReadMoveDto;
import com.pokemon.entities.Move;
import com.pokemon.repositories.MoveRepository;
import com.pokemon.services.service.AbstractQueryService;
import com.pokemon.services.service.SpecificationBuilder;
import com.pokemon.utils.enums.SearchOperation;
import com.pokemon.utils.mappers.move.MoveMapper;

import jakarta.persistence.criteria.JoinType;

@Service
public class MoveQueryService extends AbstractQueryService<Move, Long, MoveRepository, ReadMoveDto, MoveMapper> {

    public MoveQueryService(MoveRepository repository, MoveMapper mapper) {
        super(mapper, repository);
    }

    public Page<ReadMoveDto> filterMoves(MoveFilterDto filter, @NonNull Pageable pageable) {
        Specification<Move> specification = buildSpecification(filter);

        Specification<Move> fetchSpec = (root, query, cb) -> {
            if (query != null && !query.getResultType().equals(Long.class)) {
                root.fetch("type", JoinType.LEFT);
                query.distinct(true);
            }
            return cb.conjunction();
        };

        Specification<Move> finalSpec = specification == null ? fetchSpec : specification.and(fetchSpec);

        Page<Move> movePage = repository.findAll(finalSpec, pageable);

        return movePage.map(mapper::toDto);
    }

    private Specification<Move> buildSpecification(MoveFilterDto filter) {
        SpecificationBuilder<Move> specBuilder = new SpecificationBuilder<>();

        if (filter.getId() != null) {
            specBuilder.with("id", filter.getId(), null, SearchOperation.EQUAL);
        }

        if (filter.getName() != null && !filter.getName().isBlank()) {
            specBuilder.with("name", filter.getName(), null, SearchOperation.LIKE);
        }

        if (filter.getNameExact() != null && !filter.getNameExact().isBlank()) {
            specBuilder.with("name", filter.getNameExact(), null, SearchOperation.EQUAL);
        }

        if (filter.getTypeId() != null) {
            specBuilder.with("type.id", filter.getTypeId(), null, SearchOperation.EQUAL);
        }

        if (filter.getMinAccuracy() != null && filter.getMaxAccuracy() != null) {
            specBuilder.between("accuracy", filter.getMinAccuracy(), filter.getMaxAccuracy());
        } else {
            if (filter.getMinAccuracy() != null) {
                specBuilder.with("accuracy", filter.getMinAccuracy(), null, SearchOperation.GREATER_THAN_OR_EQUAL);
            }
            if (filter.getMaxAccuracy() != null) {
                specBuilder.with("accuracy", filter.getMaxAccuracy(), null, SearchOperation.LESS_THAN_OR_EQUAL);
            }
        }

        if (filter.getMinPower() != null && filter.getMaxPower() != null) {
            specBuilder.between("power", filter.getMinPower(), filter.getMaxPower());
        } else {
            if (filter.getMinPower() != null) {
                specBuilder.with("power", filter.getMinPower(), null, SearchOperation.GREATER_THAN_OR_EQUAL);
            }
            if (filter.getMaxPower() != null) {
                specBuilder.with("power", filter.getMaxPower(), null, SearchOperation.LESS_THAN_OR_EQUAL);
            }
        }

        if (filter.getMinPp() != null && filter.getMaxPp() != null) {
            specBuilder.between("pp", filter.getMinPp(), filter.getMaxPp());
        } else {
            if (filter.getMinPp() != null) {
                specBuilder.with("pp", filter.getMinPp(), null, SearchOperation.GREATER_THAN_OR_EQUAL);
            }
            if (filter.getMaxPp() != null) {
                specBuilder.with("pp", filter.getMaxPp(), null, SearchOperation.LESS_THAN_OR_EQUAL);
            }
        }

        if (filter.getMinPriority() != null && filter.getMaxPriority() != null) {
            specBuilder.between("priority", filter.getMinPriority(), filter.getMaxPriority());
        } else {
            if (filter.getMinPriority() != null) {
                specBuilder.with("priority", filter.getMinPriority(), null, SearchOperation.GREATER_THAN_OR_EQUAL);
            }
            if (filter.getMaxPriority() != null) {
                specBuilder.with("priority", filter.getMaxPriority(), null, SearchOperation.LESS_THAN_OR_EQUAL);
            }
        }

        if (filter.getDamageClass() != null) {
            specBuilder.with("damageClass", filter.getDamageClass(), null, SearchOperation.EQUAL);
        }

        if (filter.getHasPower() != null) {
            if (filter.getHasPower()) {
                specBuilder.with("power", null, null, SearchOperation.IS_NOT_NULL);
            } else {
                specBuilder.with("power", null, null, SearchOperation.IS_NULL);
            }
        }

        if (filter.getHasAccuracy() != null) {
            if (filter.getHasAccuracy()) {
                specBuilder.with("accuracy", null, null, SearchOperation.IS_NOT_NULL);
            } else {
                specBuilder.with("accuracy", null, null, SearchOperation.IS_NULL);
            }
        }

        if (filter.getHasEffectChance() != null) {
            if (filter.getHasEffectChance()) {
                specBuilder.with("effectChance", null, null, SearchOperation.IS_NOT_NULL);
            } else {
                specBuilder.with("effectChance", null, null, SearchOperation.IS_NULL);
            }
        }

        return specBuilder.build();
    }

    @Override
    protected Class<Move> getEntityClass() {
        return Move.class;
    }
}
