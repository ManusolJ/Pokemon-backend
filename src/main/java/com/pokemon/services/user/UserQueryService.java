package com.pokemon.services.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.pokemon.dtos.user.ReadUserDto;
import com.pokemon.dtos.user.UserFilterDto;
import com.pokemon.entities.User;
import com.pokemon.repositories.UserRepository;
import com.pokemon.services.service.AbstractQueryService;
import com.pokemon.services.service.SpecificationBuilder;
import com.pokemon.utils.enums.SearchOperation;
import com.pokemon.utils.mappers.UserMapper;

@Service
@Validated
@Transactional(readOnly = true)
public class UserQueryService extends AbstractQueryService<User, Long, UserRepository, ReadUserDto, UserMapper> {

    public UserQueryService(UserMapper mapper, UserRepository repository) {
        super(mapper, repository, User.class);
    }

    public Page<ReadUserDto> filterUsers(UserFilterDto filter, @NonNull Pageable pageable) {
        Specification<User> specification = buildSpecification(filter);
        return repository.findAll(specification, pageable)
                .map(mapper::toDto);
    }

    public long countWithFilters(UserFilterDto filter) {
        Specification<User> specification = buildSpecification(filter);
        return repository.count(specification);
    }

    private Specification<User> buildSpecification(UserFilterDto filter) {
        SpecificationBuilder<User> specBuilder = new SpecificationBuilder<>();

        if (filter.getUsername() != null && !filter.getUsername().isBlank()) {
            specBuilder.with("username", filter.getUsername(), null, SearchOperation.LIKE);
        }

        if (filter.getUsernameExact() != null && !filter.getUsernameExact().isBlank()) {
            specBuilder.with("username", filter.getUsernameExact(), null, SearchOperation.EQUAL);
        }

        if (filter.getRole() != null) {
            specBuilder.with("role", filter.getRole(), null, SearchOperation.EQUAL);
        }

        if (filter.getActive() != null) {
            specBuilder.with("active", filter.getActive(), null, SearchOperation.EQUAL);
        }

        if (filter.getCreatedAtFrom() != null && filter.getCreatedAtTo() != null) {
            specBuilder.between("createdAt", filter.getCreatedAtFrom(), filter.getCreatedAtTo());
        } else {
            if (filter.getCreatedAtFrom() != null) {
                specBuilder.with("createdAt", filter.getCreatedAtFrom(), null, SearchOperation.GREATER_THAN_OR_EQUAL);
            }
            if (filter.getCreatedAtTo() != null) {
                specBuilder.with("createdAt", filter.getCreatedAtTo(), null, SearchOperation.LESS_THAN_OR_EQUAL);
            }
        }

        if (filter.getUpdatedAtFrom() != null && filter.getUpdatedAtTo() != null) {
            specBuilder.between("updatedAt", filter.getUpdatedAtFrom(), filter.getUpdatedAtTo());
        } else {
            if (filter.getUpdatedAtFrom() != null) {
                specBuilder.with("updatedAt", filter.getUpdatedAtFrom(), null, SearchOperation.GREATER_THAN_OR_EQUAL);
            }
            if (filter.getUpdatedAtTo() != null) {
                specBuilder.with("updatedAt", filter.getUpdatedAtTo(), null, SearchOperation.LESS_THAN_OR_EQUAL);
            }
        }

        return specBuilder.build();
    }

}
