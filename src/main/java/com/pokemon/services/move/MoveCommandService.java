package com.pokemon.services.move;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.pokemon.dtos.rest.move.MoveRestDto;
import com.pokemon.dtos.rest.resource.PokeApiResource;
import com.pokemon.entities.Move;
import com.pokemon.entities.Type;
import com.pokemon.repositories.MoveRepository;
import com.pokemon.services.service.AbstractCommandService;
import com.pokemon.services.service.IdentityMapService;
import com.pokemon.utils.enums.CacheKey;
import com.pokemon.utils.mappers.move.MoveApiMapper;

@Service
public class MoveCommandService extends AbstractCommandService<Move, Long, MoveRepository, MoveRestDto, MoveApiMapper> {
    public MoveCommandService(MoveApiMapper apiMapper, MoveRepository repository, RestClient restClient,
            IdentityMapService cacheService) {
        super(apiMapper, repository, restClient, cacheService);
    }

    @Override
    protected String getEntityName() {
        return "move";
    }

    @Override
    protected CacheKey getCacheKey() {
        return CacheKey.MOVE;
    }

    @Override
    protected String getResourcePath() {
        return "/move?limit=20";
    }

    @Override
    protected Class<MoveRestDto> getApiDtoClass() {
        return MoveRestDto.class;
    }

    @Override
    protected String extractEntityName(Move entity) {
        return entity.getName();
    }

    @Override
    protected Function<MoveRestDto, Move> getEntityConverter() {
        return null;
    }

    @Override
    public void fetchAndSave() {
        List<PokeApiResource> resources = fetchResourceList();
        int initialFetchSize = resources.size();

        List<Move> entities = new ArrayList<>();

        List<MoveRestDto> dtos = resources.stream()
                .map(this::fetchApiDto)
                .filter(Objects::nonNull)
                .toList();

        for (MoveRestDto dto : dtos) {
            Move move = apiMapper.toEntity(dto);
            String typeName = dto.getType().getName();
            Type type = cacheService.get(CacheKey.TYPE, typeName);
            if (type == null) {
                throw new IllegalStateException(
                        "Type with name " + typeName + " not found in cache when mapping move " + move.getName());
            }
            move.setType(type);
            entities.add(move);
        }

        entities = entities.stream()
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        saveAllAndLog(entities, initialFetchSize);
    }
}
