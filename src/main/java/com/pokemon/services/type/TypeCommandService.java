package com.pokemon.services.type;

import java.util.function.Function;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.pokemon.dtos.rest.types.TypeRestDto;
import com.pokemon.entities.Type;
import com.pokemon.repositories.TypeRepository;
import com.pokemon.services.service.AbstractCommandService;
import com.pokemon.services.service.IdentityMapService;
import com.pokemon.utils.enums.CacheKey;
import com.pokemon.utils.mappers.type.TypeApiMapper;

@Service
public class TypeCommandService
        extends AbstractCommandService<Type, Long, TypeRepository, TypeRestDto, TypeApiMapper> {

    public TypeCommandService(TypeRepository repository, TypeApiMapper mapper, RestClient restClient,
            IdentityMapService cacheService) {
        super(mapper, repository, restClient, cacheService);
    }

    @Override
    protected CacheKey getCacheKey() {
        return CacheKey.TYPE;
    }

    @Override
    protected String extractEntityName(Type entity) {
        return entity.getName();
    }

    @Override
    protected String getResourcePath() {
        return "/type?limit=20";
    }

    @Override
    protected Class<TypeRestDto> getApiDtoClass() {
        return TypeRestDto.class;
    }

    @Override
    protected String getEntityName() {
        return "types";
    }

    @Override
    protected Function<TypeRestDto, Type> getEntityConverter() {
        return dto -> apiMapper.toEntity(dto);
    }
}
