package com.pokemon.services.service;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import org.springframework.stereotype.Service;

import com.pokemon.utils.enums.CacheKey;

@Service
public class IdentityMapService {

    private final Map<CacheKey, Map<Object, Object>> store = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public <T> T getOrCreate(
            CacheKey key,
            Object externalId,
            Supplier<T> factory) {

        Map<Object, Object> map = store.computeIfAbsent(key, k -> new ConcurrentHashMap<>());

        return (T) map.computeIfAbsent(externalId, id -> factory.get());
    }

    @SuppressWarnings("unchecked")
    public <T> T get(CacheKey key, Object externalId) {
        return (T) store
                .getOrDefault(key, Map.of())
                .get(externalId);
    }

    @SuppressWarnings("unchecked")
    public <T> Collection<T> getAll(CacheKey key) {
        return store
                .getOrDefault(key, Map.of())
                .values()
                .stream()
                .map(e -> (T) e)
                .toList();
    }

    public boolean cacheExists(CacheKey key) {
        return store.containsKey(key);
    }

    public void clear() {
        store.clear();
    }

    public void clearAll() {
        for (CacheKey key : store.keySet()) {
            store.get(key).clear();
        }
    }
}
