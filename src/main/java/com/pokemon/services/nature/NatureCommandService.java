package com.pokemon.services.nature;

import java.util.function.Function;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.pokemon.dtos.rest.nature.NatureRestDto;
import com.pokemon.entities.Nature;
import com.pokemon.repositories.NatureRepository;
import com.pokemon.services.service.AbstractCommandService;
import com.pokemon.services.service.IdentityMapService;
import com.pokemon.utils.enums.CacheKey;
import com.pokemon.utils.mappers.nature.NatureApiMapper;

@Service
public class NatureCommandService
        extends AbstractCommandService<Nature, Long, NatureRepository, NatureRestDto, NatureApiMapper> {

    public NatureCommandService(NatureApiMapper apiMapper, NatureRepository repository, RestClient restClient,
            IdentityMapService cacheService) {
        super(apiMapper, repository, restClient, cacheService);
    }

    @Override
    protected String getEntityName() {
        return "Nature";
    }

    @Override
    protected CacheKey getCacheKey() {
        return CacheKey.NATURE;
    }

    @Override
    protected String getResourcePath() {
        return "/nature?limit=20";
    }

    @Override
    protected Class<NatureRestDto> getApiDtoClass() {
        return NatureRestDto.class;
    }

    @Override
    protected String extractEntityName(Nature entity) {
        return entity.getName();
    }

    @Override
    protected Function<NatureRestDto, Nature> getEntityConverter() {
        return dto -> apiMapper.toEntity(dto);
    }

}
