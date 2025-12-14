package com.pokemon.services.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.pokemon.utils.enums.CacheKey;

@Service
public class CacheService {

    private final Map<CacheKey, Map<String, Long>> entityCaches = new ConcurrentHashMap<>();

    public Map<String, Long> getCache(CacheKey key) {
        return entityCaches.computeIfAbsent(key, k -> new ConcurrentHashMap<>());
    }

    public void loadToCache(CacheKey key, String name, Long id) {
        if (name != null && id != null) {
            getCache(key).put(name, id);
        }
    }

    public void loadAllToCache(CacheKey key, Map<String, Long> nameToIdMap) {
        if (nameToIdMap != null) {
            getCache(key).putAll(nameToIdMap);
        }
    }

    public Long getIdFromCache(CacheKey key, String name) {
        return getCache(key).get(name);
    }

    public void clearCache(CacheKey key) {
        getCache(key).clear();
    }

    public void clearAllCaches() {
        entityCaches.clear();
    }

    public boolean cacheExists(CacheKey key) {
        return entityCaches.containsKey(key);
    }

    public boolean isInCache(CacheKey key, String name) {
        return getCache(key).containsKey(name);
    }

    public int getCacheSize(CacheKey key) {
        return getCache(key).size();
    }
}
