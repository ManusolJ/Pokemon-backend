package com.pokemon.services.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.pokemon.dtos.rest.item.ItemCategoryRestDto;
import com.pokemon.dtos.rest.item.ItemRestDto;
import com.pokemon.dtos.rest.resource.PokeApiResource;
import com.pokemon.entities.Item;
import com.pokemon.repositories.ItemRepository;
import com.pokemon.services.service.AbstractCommandService;
import com.pokemon.services.service.IdentityMapService;
import com.pokemon.utils.enums.CacheKey;
import com.pokemon.utils.enums.HeldItemCategory;
import com.pokemon.utils.mappers.item.ItemApiMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ItemCommandService extends AbstractCommandService<Item, Long, ItemRepository, ItemRestDto, ItemApiMapper> {

    public ItemCommandService(ItemApiMapper apiMapper, ItemRepository repository, RestClient restClient,
            IdentityMapService cacheService) {
        super(apiMapper, repository, restClient, cacheService);

    }

    @Override
    protected String getEntityName() {
        return "item";
    }

    @Override
    protected CacheKey getCacheKey() {
        return CacheKey.ITEM;
    }

    @Override
    protected String getResourcePath() {
        return "/item-category?limit=100";
    }

    @Override
    protected Class<ItemRestDto> getApiDtoClass() {
        return ItemRestDto.class;
    }

    @Override
    protected String extractEntityName(Item entity) {
        return entity.getName();
    }

    @Override
    protected Function<ItemRestDto, Item> getEntityConverter() {
        return dto -> apiMapper.toEntity(dto);
    }

    @Override
    public void fetchAndSave() {
        List<PokeApiResource> resources = fetchResourceList();

        resources = resources.stream()
                .filter(resource -> HeldItemCategory.isHeldItemCategory(resource.getName()))
                .distinct()
                .toList();

        log.info("Fetched {} {} resources from PokéAPI", resources.size(), getEntityName());

        List<PokeApiResource> individualResources = new ArrayList<>();

        for (PokeApiResource resource : resources) {
            String resourcePath = "/item-category/" + resource.getName();
            ItemCategoryRestDto categoryResource = restClient.get()
                    .uri(resourcePath).retrieve()
                    .body(ItemCategoryRestDto.class);

            if (categoryResource != null && categoryResource.getItems() != null) {
                individualResources.addAll(categoryResource.getItems());
            }
        }

        List<Item> entities = individualResources.stream()
                .filter(Objects::nonNull)
                .map(this::fetchApiDto)
                .filter(Objects::nonNull)
                .map(getEntityConverter())
                .filter(Objects::nonNull)
                .toList();

        int initialFetchSize = entities.size();

        saveAllAndLog(entities, initialFetchSize);
    }

}
