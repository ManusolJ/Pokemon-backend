package com.pokemon.services.type;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.pokemon.dtos.rest.ResourceDto;
import com.pokemon.dtos.rest.ResourceListDto;
import com.pokemon.dtos.rest.TypeRestDto;
import com.pokemon.entities.Type;
import com.pokemon.repositories.TypeRepository;
import com.pokemon.utils.mappers.TypeMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TypeCommandService {

    private final RestClient restClient;
    private final TypeMapper typeMapper;
    private final TypeRepository typeRepository;
    private final Executor ioPool = Executors
            .newFixedThreadPool(Math.max(4, Runtime.getRuntime().availableProcessors()));

    public void fetchAndSaveTypes() {
        final ResourceListDto typeList = fetchTypeList();
        if (typeList == null || typeList.getResults() == null) {
            throw new IllegalStateException("Failed to fetch types from PokéAPI");
        }

        final List<CompletableFuture<Type>> futures = typeList.getResults().stream()
                .filter(this::isValidResource)
                .map(res -> CompletableFuture.supplyAsync(() -> fetchAndConvertType(res), ioPool)
                        .exceptionally(ex -> {
                            log.warn("Failed to fetch type: {}", res.getName(), ex);
                            return null;
                        }))
                .toList();

        final List<Type> fetched = futures.stream()
                .map(CompletableFuture::join)
                .filter(Objects::nonNull)
                .toList();

        saveTypesInBatch(fetched);
    }

    private ResourceListDto fetchTypeList() {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder.path("/type").queryParam("limit", 1000).build())
                .retrieve()
                .body(ResourceListDto.class);
    }

    @Transactional
    private void saveTypesInBatch(List<Type> types) {
        if (types == null || types.isEmpty()) {
            log.info("No types to save");
            return;
        }

        final Set<String> candidateNames = types.stream()
                .map(Type::getName)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        if (candidateNames.isEmpty()) {
            log.info("No valid type names to save");
            return;
        }

        final List<Long> existingIds = typeRepository.findAllIds();
        final List<Type> newTypes = types.stream()
                .filter(t -> t.getId() != null && !existingIds.contains(t.getId()))
                .toList();

        if (newTypes.isEmpty()) {
            log.info("No new types to save");
            return;
        }

        typeRepository.saveAll(newTypes);
        log.info("Saved {} new types: {}", newTypes.size(),
                newTypes.stream().map(Type::getName).toList());
    }

    private Type fetchAndConvertType(ResourceDto resource) {
        try {
            final String rawUrl = resource.getUrl();
            if (rawUrl == null || rawUrl.trim().isEmpty()) {
                log.warn("Invalid URL for resource: {}", resource.getName());
                return null;
            }

            final TypeRestDto dto = restClient.get().uri(rawUrl).retrieve().body(TypeRestDto.class);
            if (dto == null || dto.getName() == null)
                return null;

            return typeMapper.toEntity(dto);

        } catch (Exception e) {
            log.warn("Failed to convert type from resource: {}", resource.getName(), e);
            return null;
        }
    }

    private boolean isValidResource(ResourceDto r) {
        return r != null &&
                r.getName() != null && !r.getName().trim().isEmpty() &&
                r.getUrl() != null && !r.getUrl().trim().isEmpty();
    }
}
