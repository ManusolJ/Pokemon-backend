package com.pokemon.services.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.pokemon.dtos.pokemon.item.ItemFilterDto;
import com.pokemon.dtos.pokemon.item.ReadItemDto;
import com.pokemon.entities.Item;
import com.pokemon.repositories.ItemRepository;
import com.pokemon.services.service.AbstractQueryService;
import com.pokemon.services.service.SpecificationBuilder;
import com.pokemon.utils.enums.SearchOperation;
import com.pokemon.utils.mappers.item.ItemMapper;

import jakarta.validation.constraints.NotNull;

@Service
public class ItemQueryService extends AbstractQueryService<Item, Long, ItemRepository, ReadItemDto, ItemMapper> {

    public ItemQueryService(ItemMapper mapper, ItemRepository repository) {
        super(mapper, repository);
    }

    public Page<ReadItemDto> filterItems(ItemFilterDto filter, @NotNull Pageable pageable) {
        Specification<Item> specification = buildSpecification(filter);

        final Pageable validatedPageable = pageable != null ? pageable : Pageable.ofSize(20);

        Page<Item> itemPage = repository.findAll(specification, validatedPageable);

        return itemPage.map(mapper::toDto);
    }

    private Specification<Item> buildSpecification(ItemFilterDto filter) {
        SpecificationBuilder<Item> specBuilder = new SpecificationBuilder<>();

        if (filter.getId() != null) {
            specBuilder.with("id", filter.getId(), null, SearchOperation.EQUAL);
        }

        if (filter.getName() != null && !filter.getName().isBlank()) {
            specBuilder.with("name", filter.getName(), null, SearchOperation.LIKE);
        }

        if (filter.getNameExact() != null && !filter.getNameExact().isBlank()) {
            specBuilder.with("name", filter.getNameExact(), null, SearchOperation.EQUAL);
        }

        return specBuilder.build();
    }

    @Override
    protected Class<Item> getEntityClass() {
        return Item.class;
    }

}
