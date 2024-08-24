package org.uhanov.repository;

import org.uhanov.model.EntityWithUUID;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public abstract class AbstractRepositoryMock<T extends EntityWithUUID> {
    protected Map<UUID, T> entities = new HashMap<>();

    public T addEntity(T entity) {
        entities.put(entity.getId(), entity);
        return entity;
    }

    public Optional<T> getByUUID(UUID uuid) {
        return Optional.ofNullable(entities.get(uuid));
    }

    public boolean removeByUUID(UUID uuid) {
        return entities.remove(uuid, entities.get(uuid));
    }
}

