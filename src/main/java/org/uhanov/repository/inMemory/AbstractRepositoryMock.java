package org.uhanov.repository.inMemory;

import org.uhanov.model.EntityWithUUID;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractRepositoryMock<T extends EntityWithUUID> {
    protected Map<UUID, T> entities = new ConcurrentHashMap<>();

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

