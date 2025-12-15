package com.pokemon.services.pokemon;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.pokemon.dtos.pokemon.pokemon.PokemonFilterDto;
import com.pokemon.dtos.pokemon.pokemon.ReadPokemonDto;
import com.pokemon.dtos.pokemon.species.ReducedReadPokemonSpeciesDto;
import com.pokemon.dtos.pokemon.type.ReadTypeDto;
import com.pokemon.entities.Pokemon;
import com.pokemon.repositories.PokemonRepository;
import com.pokemon.services.service.AbstractQueryService;
import com.pokemon.services.service.SpecificationBuilder;
import com.pokemon.utils.enums.SearchOperation;
import com.pokemon.utils.mappers.pokemon.PokemonMapper;
import com.pokemon.utils.mappers.species.PokemonSpeciesMapper;
import com.pokemon.utils.mappers.type.TypeMapper;

import jakarta.persistence.criteria.JoinType;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PokemonQueryService
        extends AbstractQueryService<Pokemon, Long, PokemonRepository, ReadPokemonDto, PokemonMapper> {

    private final TypeMapper typeMapper;
    private final PokemonSpeciesMapper speciesMapper;

    public PokemonQueryService(PokemonMapper mapper, TypeMapper typeMapper, PokemonSpeciesMapper speciesMapper,
            PokemonRepository repository) {
        super(mapper, repository);
        this.typeMapper = typeMapper;
        this.speciesMapper = speciesMapper;
    }

    public Page<ReadPokemonDto> filterPokemon(PokemonFilterDto filter, @NotNull Pageable pageable) {
        Specification<Pokemon> specification = buildSpecification(filter);

        Specification<Pokemon> fetchSpec = (root, query, cb) -> {
            if (query != null && !query.getResultType().equals(Long.class)) {
                root.fetch("species", JoinType.LEFT);
                root.fetch("primaryType", JoinType.LEFT);
                root.fetch("secondaryType", JoinType.LEFT);
                query.distinct(true);
            }
            return cb.conjunction();
        };

        final Pageable validPageable = pageable != null ? pageable : Pageable.ofSize(20);

        final Specification<Pokemon> finalSpec = specification == null ? fetchSpec : specification.and(fetchSpec);

        Page<Pokemon> pokemonPage = repository.findAll(finalSpec, validPageable);

        return pokemonPage.map(pokemon -> {
            ReadPokemonDto dto = mapper.toDto(pokemon);
            ReadTypeDto primaryType = typeMapper.toDto(pokemon.getPrimaryType());
            ReadTypeDto secondaryType = null;
            if (pokemon.getSecondaryType() != null) {
                secondaryType = typeMapper.toDto(pokemon.getSecondaryType());
            }
            dto.setPrimaryType(primaryType);
            dto.setSecondaryType(secondaryType);
            ReducedReadPokemonSpeciesDto speciesDto = speciesMapper.toReducedDto(pokemon.getSpecies());
            dto.setSpecies(speciesDto);
            return dto;
        });
    }

    private Specification<Pokemon> buildSpecification(PokemonFilterDto filter) {
        SpecificationBuilder<Pokemon> specBuilder = new SpecificationBuilder<>();

        if (filter.getId() != null) {
            specBuilder.with("id", filter.getId(), null, SearchOperation.EQUAL);
        }

        if (filter.getSpecies() != null) {
            specBuilder.with("species.id", filter.getSpecies(), null, SearchOperation.EQUAL);
        }

        if (filter.getName() != null && !filter.getName().isBlank()) {
            specBuilder.with("name", filter.getName(), null, SearchOperation.LIKE);
        }

        if (filter.getNameExact() != null && !filter.getNameExact().isBlank()) {
            specBuilder.with("name", filter.getNameExact(), null, SearchOperation.EQUAL);
        }

        if (filter.getFormName() != null && !filter.getFormName().isBlank()) {
            specBuilder.with("formName", filter.getFormName(), null, SearchOperation.LIKE);
        }

        if (filter.getIsDefault() != null) {
            specBuilder.with("isDefault", filter.getIsDefault(), null, SearchOperation.EQUAL);
        }

        if (filter.getPrimaryType() != null) {
            specBuilder.with("primaryType.id", filter.getPrimaryType(), null, SearchOperation.EQUAL);
        }

        if (filter.getSecondaryType() != null) {
            specBuilder.with("secondaryType.id", filter.getSecondaryType(), null, SearchOperation.EQUAL);
        }

        if (filter.getAbilityId() != null) {
            specBuilder.with("ability", filter.getAbilityId(), null, SearchOperation.EQUAL);
        }

        if (filter.getHeightMin() != null && filter.getHeightMax() != null) {
            specBuilder.between("height", filter.getHeightMin(), filter.getHeightMax());
        } else {
            if (filter.getHeightMin() != null) {
                specBuilder.with("height", filter.getHeightMin(), null, SearchOperation.GREATER_THAN_OR_EQUAL);
            }
            if (filter.getHeightMax() != null) {
                specBuilder.with("height", filter.getHeightMax(), null, SearchOperation.LESS_THAN_OR_EQUAL);
            }
        }

        if (filter.getWeightMin() != null && filter.getWeightMax() != null) {
            specBuilder.between("weight", filter.getWeightMin(), filter.getWeightMax());
        } else {
            if (filter.getWeightMin() != null) {
                specBuilder.with("weight", filter.getWeightMin(), null, SearchOperation.GREATER_THAN_OR_EQUAL);
            }
            if (filter.getWeightMax() != null) {
                specBuilder.with("weight", filter.getWeightMax(), null, SearchOperation.LESS_THAN_OR_EQUAL);
            }
        }

        if (filter.getMinHp() != null && filter.getMaxHp() != null) {
            specBuilder.between("hp", filter.getMinHp(), filter.getMaxHp());
        } else {
            if (filter.getMinHp() != null) {
                specBuilder.with("hp", filter.getMinHp(), null, SearchOperation.GREATER_THAN_OR_EQUAL);
            }
            if (filter.getMaxHp() != null) {
                specBuilder.with("hp", filter.getMaxHp(), null, SearchOperation.LESS_THAN_OR_EQUAL);
            }
        }

        if (filter.getMinAttack() != null && filter.getMaxAttack() != null) {
            specBuilder.between("attack", filter.getMinAttack(), filter.getMaxAttack());
        } else {
            if (filter.getMinAttack() != null) {
                specBuilder.with("attack", filter.getMinAttack(), null, SearchOperation.GREATER_THAN_OR_EQUAL);
            }
            if (filter.getMaxAttack() != null) {
                specBuilder.with("attack", filter.getMaxAttack(), null, SearchOperation.LESS_THAN_OR_EQUAL);
            }
        }

        if (filter.getMinDefense() != null && filter.getMaxDefense() != null) {
            specBuilder.between("defense", filter.getMinDefense(), filter.getMaxDefense());
        } else {
            if (filter.getMinDefense() != null) {
                specBuilder.with("defense", filter.getMinDefense(), null, SearchOperation.GREATER_THAN_OR_EQUAL);
            }
            if (filter.getMaxDefense() != null) {
                specBuilder.with("defense", filter.getMaxDefense(), null, SearchOperation.LESS_THAN_OR_EQUAL);
            }
        }

        if (filter.getMinSpecialAttack() != null && filter.getMaxSpecialAttack() != null) {
            specBuilder.between("specialAttack", filter.getMinSpecialAttack(), filter.getMaxSpecialAttack());
        } else {
            if (filter.getMinSpecialAttack() != null) {
                specBuilder.with("specialAttack", filter.getMinSpecialAttack(), null,
                        SearchOperation.GREATER_THAN_OR_EQUAL);
            }
            if (filter.getMaxSpecialAttack() != null) {
                specBuilder.with("specialAttack", filter.getMaxSpecialAttack(), null,
                        SearchOperation.LESS_THAN_OR_EQUAL);
            }
        }

        if (filter.getMinSpecialDefense() != null && filter.getMaxSpecialDefense() != null) {
            specBuilder.between("specialDefense", filter.getMinSpecialDefense(), filter.getMaxSpecialDefense());
        } else {
            if (filter.getMinSpecialDefense() != null) {
                specBuilder.with("specialDefense", filter.getMinSpecialDefense(), null,
                        SearchOperation.GREATER_THAN_OR_EQUAL);
            }
            if (filter.getMaxSpecialDefense() != null) {
                specBuilder.with("specialDefense", filter.getMaxSpecialDefense(), null,
                        SearchOperation.LESS_THAN_OR_EQUAL);
            }
        }

        if (filter.getMinSpeed() != null && filter.getMaxSpeed() != null) {
            specBuilder.between("speed", filter.getMinSpeed(), filter.getMaxSpeed());
        } else {
            if (filter.getMinSpeed() != null) {
                specBuilder.with("speed", filter.getMinSpeed(), null, SearchOperation.GREATER_THAN_OR_EQUAL);
            }
            if (filter.getMaxSpeed() != null) {
                specBuilder.with("speed", filter.getMaxSpeed(), null, SearchOperation.LESS_THAN_OR_EQUAL);
            }
        }

        return specBuilder.build();
    }

    protected Class<Pokemon> getEntityClass() {
        return Pokemon.class;
    }

}
