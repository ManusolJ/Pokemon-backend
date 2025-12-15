package com.pokemon.services.ability;

import java.util.function.Function;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.pokemon.dtos.rest.ability.AbilityRestDto;
import com.pokemon.entities.Ability;
import com.pokemon.repositories.AbilityRepository;
import com.pokemon.services.service.AbstractCommandService;
import com.pokemon.services.service.IdentityMapService;
import com.pokemon.utils.enums.CacheKey;
import com.pokemon.utils.mappers.ability.AbilityApiMapper;

@Service
public class AbilityCommandService
        extends AbstractCommandService<Ability, Long, AbilityRepository, AbilityRestDto, AbilityApiMapper> {

    public AbilityCommandService(AbilityApiMapper apiMapper, AbilityRepository repository, RestClient restClient,
            IdentityMapService cacheService) {
        super(apiMapper, repository, restClient, cacheService);

    }

    @Override
    protected String getEntityName() {
        return "Ability";
    }

    @Override
    protected CacheKey getCacheKey() {
        return CacheKey.ABILITY;
    }

    @Override
    protected String getResourcePath() {
        return "/ability?limit=20";
    }

    @Override
    protected Class<AbilityRestDto> getApiDtoClass() {
        return AbilityRestDto.class;
    }

    @Override
    protected String extractEntityName(Ability entity) {
        return entity.getName();
    }

    @Override
    protected Function<AbilityRestDto, Ability> getEntityConverter() {
        return dto -> apiMapper.toEntity(dto);
    }
}
