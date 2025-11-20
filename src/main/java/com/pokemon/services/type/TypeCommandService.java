package com.pokemon.services.type;

import java.net.URI;
import java.util.List;
import java.util.Objects;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.pokemon.dtos.rest.PokeApiResource;
import com.pokemon.dtos.rest.PokeApiResourceListDto;
import com.pokemon.dtos.rest.TypeRestDto;
import com.pokemon.entities.Type;
import com.pokemon.repositories.TypeRepository;
import com.pokemon.utils.mappers.TypeMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TypeCommandService {

    private final RestClient restClient;
    private final TypeMapper typeMapper;
    private final TypeRepository typeRepository;

    public void fetchAndSaveTypes() {
        PokeApiResourceListDto resourceList = restClient.get()
                .uri("/type?limit=1000")
                .retrieve()
                .body(PokeApiResourceListDto.class);

        if (resourceList == null || resourceList.getResults() == null) {
            throw new IllegalStateException("Failed to fetch types from PokéAPI");
        }

        int initialFetchSize = resourceList.getResults().size();

        List<Type> entityList = resourceList.getResults().stream()
                .map(this::fetchAndMap)
                .filter(Objects::nonNull)
                .toList();
        if (entityList.isEmpty()) {
            throw new IllegalStateException("No types were fetched and mapped from PokéAPI");
        }

        typeRepository.saveAll(entityList);

        log.info("Fetched and saved {} types from PokéAPI", entityList.size());
        if (initialFetchSize != entityList.size()) {
            log.warn("Mismatch in fetched types: initial fetch size was {}, but saved size is {}",
                    initialFetchSize, entityList.size());
        }
    }

    private Type fetchAndMap(@NonNull PokeApiResource resource) {
        URI uri = URI.create(resource.getUrl());
        String path = uri.getPath().replaceFirst("/api/v2", "");

        if (path.isBlank()) {
            return null;
        }

        TypeRestDto dto = restClient.get()
                .uri(path)
                .retrieve()
                .body(TypeRestDto.class);

        if (dto == null) {
            return null;
        }

        return typeRepository.findById(dto.getId()).orElseGet(() -> typeMapper.toEntity(dto));
    }
}
