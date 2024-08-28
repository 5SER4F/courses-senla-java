package org.uhanov.repository;

import org.uhanov.model.EntityWithUUID;

import java.util.*;

public abstract class AbstractRepositoryMock<T extends EntityWithUUID> {
    protected Map<UUID, T> entities = new HashMap<>();

    public T saveEntity(T entity) {
        entities.put(entity.getId(), entity);
        return entity;
    }

    public Optional<T> getByUUID(UUID uuid) {
        return Optional.ofNullable(entities.get(uuid));
    }

    public boolean removeByUUID(UUID uuid) {
        return entities.remove(uuid, entities.get(uuid));
    }

    public Collection<T> getAll() {
        return entities.values();
    }
}

