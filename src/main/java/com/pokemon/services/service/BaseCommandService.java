package com.pokemon.services.service;

import java.util.List;

public interface BaseCommandService<E, ID> {

    void fetchAndSave();

    void saveAllAndLog(List<E> entities, int initialFetchSize);
}
