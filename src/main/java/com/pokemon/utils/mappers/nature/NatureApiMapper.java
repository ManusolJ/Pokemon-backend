package com.pokemon.utils.mappers.nature;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.pokemon.dtos.rest.nature.NatureRestDto;
import com.pokemon.dtos.rest.resource.PokeApiResource;
import com.pokemon.entities.Nature;
import com.pokemon.utils.mappers.mapper.BaseMapper;
import com.pokemon.utils.mappers.mapper.BaseMapperConfig;

@Mapper(componentModel = "spring", config = BaseMapperConfig.class)
public interface NatureApiMapper extends BaseMapper<Nature, NatureRestDto> {

    @Mapping(target = "increasedStat", source = "increasedStat", qualifiedByName = "getStat")
    @Mapping(target = "decreasedStat", source = "decreasedStat", qualifiedByName = "getStat")
    Nature toEntity(NatureRestDto dto);

    @Named("getStat")
    static String getStat(PokeApiResource stat) {
        if (stat != null) {
            return stat.getName();
        }
        return null;
    }
}
