package com.pokemon.services.pokemon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.pokemon.dtos.rest.ability.PokemonAbilityRestDto;
import com.pokemon.dtos.rest.move.MoveLearneableByPokemonRestDto;
import com.pokemon.dtos.rest.pokemon.PokemonRestDto;
import com.pokemon.dtos.rest.pokemon.StatRestDto;
import com.pokemon.dtos.rest.resource.PokeApiResource;
import com.pokemon.dtos.rest.types.TypeRestResourceDto;
import com.pokemon.entities.Ability;
import com.pokemon.entities.Move;
import com.pokemon.entities.Pokemon;
import com.pokemon.entities.PokemonAbility;
import com.pokemon.entities.PokemonSpecies;
import com.pokemon.entities.Type;
import com.pokemon.entities.CompositeIDs.PokemonAbilityId;
import com.pokemon.repositories.PokemonAbilityRepository;
import com.pokemon.repositories.PokemonRepository;
import com.pokemon.services.service.AbstractCommandService;
import com.pokemon.services.service.IdentityMapService;
import com.pokemon.utils.enums.CacheKey;
import com.pokemon.utils.mappers.pokemon.PokemonApiMapper;

@Service
public class PokemonCommandService
        extends AbstractCommandService<Pokemon, Long, PokemonRepository, PokemonRestDto, PokemonApiMapper> {

    private final PokemonAbilityRepository pokemonAbilityRepository;
    private final Map<Pokemon, List<PokemonAbilityRestDto>> pokemonAbilitiesCache = new HashMap<>();

    public PokemonCommandService(PokemonRepository repository,
            PokemonApiMapper mapper, RestClient restClient,
            IdentityMapService cacheService, PokemonAbilityRepository pokemonAbilityRepository) {
        super(mapper, repository, restClient, cacheService);
        this.pokemonAbilityRepository = pokemonAbilityRepository;
    }

    @Override
    protected CacheKey getCacheKey() {
        return CacheKey.POKEMON;
    }

    @Override
    protected String getEntityName() {
        return "pokemon";
    }

    @Override
    protected String getResourcePath() {
        return "/pokemon?limit=20";
    }

    @Override
    protected Class<PokemonRestDto> getApiDtoClass() {
        return PokemonRestDto.class;
    }

    @Override
    protected String extractEntityName(Pokemon entity) {
        return entity.getName();
    }

    @Override
    protected Function<PokemonRestDto, Pokemon> getEntityConverter() {
        return dto -> apiMapper.toEntity(dto);
    }

    @Override
    public void fetchAndSave() {
        List<PokeApiResource> resources = fetchResourceList();
        int initialFetchSize = resources.size();

        List<PokemonRestDto> dtos = resources.stream()
                .map(this::fetchApiDto)
                .filter(Objects::nonNull)
                .toList();

        List<Pokemon> entities = dtos.stream()
                .map(dto -> convertWithAdditionalData(dto))
                .filter(Objects::nonNull)
                .toList();

        saveAllAndLog(entities, initialFetchSize);
        for (Map.Entry<Pokemon, List<PokemonAbilityRestDto>> entry : pokemonAbilitiesCache.entrySet()) {
            savePokemonAbilities(entry.getKey(), entry.getValue());
        }
    }

    private Pokemon convertWithAdditionalData(PokemonRestDto dto) {
        Pokemon pokemon = apiMapper.toEntity(dto);

        final String pokemonName = dto.getName();
        final List<StatRestDto> stats = dto.getStats();
        final List<TypeRestResourceDto> dtoTypes = dto.getTypes();
        final List<MoveLearneableByPokemonRestDto> moves = dto.getMoves();

        if (stats.isEmpty() || moves.isEmpty()) {
            return null;
        }

        List<Type> types = getTypeReferenceFromCache(dtoTypes, pokemonName);

        pokemon.setPrimaryType(types.getFirst());

        if (types.size() > 1) {
            pokemon.setSecondaryType(types.get(types.size() - 1));
        }

        pokemon.setMoves(getMoveReferencesFromCache(moves, pokemonName));
        pokemon.setSpecies(getSpeciesReferenceFromCache(dto.getSpecies(), pokemonName));

        pokemonAbilitiesCache.put(pokemon, dto.getAbilities());

        return pokemon;
    }

    private List<Type> getTypeReferenceFromCache(List<TypeRestResourceDto> dtoTypes, String pokemonName) {
        List<Type> types = new ArrayList<>();

        if (dtoTypes.isEmpty()) {
            throw new IllegalStateException("Pokemon types list is empty");
        }

        final String primaryTypeName = dtoTypes.stream()
                .filter(typeDto -> typeDto.getSlot() == 1)
                .findFirst()
                .map(typeDto -> typeDto.getType().getName())
                .orElseThrow(() -> new IllegalStateException(
                        "Pokemon " + pokemonName + " does not have a primary type"));

        final Type primaryType = cacheService.get(CacheKey.TYPE, primaryTypeName);

        if (primaryType == null) {
            throw new IllegalStateException(
                    "Type " + primaryTypeName + " not found in cache for Pokemon " + pokemonName);
        }

        types.add(primaryType);

        if (dtoTypes.size() > 1) {
            final String secondaryTypeName = dtoTypes.stream()
                    .filter(typeDto -> typeDto.getSlot() == 2)
                    .findFirst()
                    .map(typeDto -> typeDto.getType().getName())
                    .orElseThrow(() -> new IllegalStateException(
                            "Pokemon " + pokemonName + " does not have a secondary type"));

            final Type secondaryType = cacheService.get(CacheKey.TYPE, secondaryTypeName);

            if (secondaryType == null) {
                throw new IllegalStateException(
                        "Type " + secondaryTypeName + " not found in cache for Pokemon " + pokemonName);
            }

            types.add(secondaryType);
        }

        return types;
    }

    private Set<Move> getMoveReferencesFromCache(List<MoveLearneableByPokemonRestDto> dtoMoves, String pokemonName) {
        Set<Move> moves = new HashSet<>();

        for (MoveLearneableByPokemonRestDto moveDto : dtoMoves) {
            final String moveName = moveDto.getMove().getName();
            final Move move = cacheService.get(CacheKey.MOVE, moveName);

            if (move == null) {
                throw new IllegalStateException(
                        "Move " + moveName + " not found in cache for Pokemon " + pokemonName);
            }

            moves.add(move);
        }

        return moves;
    }

    private List<Ability> getAbilitiesReferencesFromCache(List<PokemonAbilityRestDto> dtoAbilities,
            String pokemonName) {
        List<Ability> abilities = new ArrayList<>();

        for (PokemonAbilityRestDto abilityResource : dtoAbilities) {
            final String abilityName = abilityResource.getAbility().getName();
            final Ability ability = cacheService.get(CacheKey.ABILITY, abilityName);

            if (ability == null) {
                throw new IllegalStateException(
                        "Ability " + abilityName + " not found in cache for Pokemon " + pokemonName);
            }

            abilities.add(ability);
        }

        return abilities;
    }

    private void savePokemonAbilities(Pokemon pokemon, List<PokemonAbilityRestDto> dtoAbilities) {
        List<PokemonAbility> pokemonAbilities = new ArrayList<>();

        List<Ability> abilities = getAbilitiesReferencesFromCache(dtoAbilities, pokemon.getName());

        if (abilities.isEmpty()) {
            throw new IllegalStateException("Pokemon abilities list is empty");
        }

        for (PokemonAbilityRestDto dto : dtoAbilities) {
            final String abilityName = dto.getAbility().getName();
            final Ability ability = abilities.stream()
                    .filter(ab -> ab.getName().equals(abilityName))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException(
                            "Ability " + abilityName + " not found in abilities list for Pokemon "
                                    + pokemon.getName()));

            final byte slot = dto.getSlot();
            final boolean isHidden = dto.isHidden();

            PokemonAbility pokemonAbility = new PokemonAbility();
            PokemonAbilityId pokemonAbilityId = new PokemonAbilityId();
            pokemonAbilityId.setPokemonId(pokemon.getId());
            pokemonAbilityId.setAbilityId(ability.getId());
            pokemonAbility.setId(pokemonAbilityId);
            pokemonAbility.setPokemon(pokemon);
            pokemonAbility.setAbility(ability);
            pokemonAbility.setSlot(slot);
            pokemonAbility.setHidden(isHidden);
            pokemonAbilities.add(pokemonAbility);
        }

        pokemonAbilityRepository.saveAll(pokemonAbilities);
    }

    private PokemonSpecies getSpeciesReferenceFromCache(PokeApiResource speciesResource, String pokemonName) {
        final PokemonSpecies species = cacheService.get(CacheKey.POKEMON_SPECIES, speciesResource.getName());

        if (species == null) {
            throw new IllegalStateException(
                    "PokemonSpecies " + speciesResource.getName() + " not found in cache for Pokemon " + pokemonName);
        }

        return species;
    }
}
