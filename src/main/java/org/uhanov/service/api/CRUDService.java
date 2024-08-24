package org.uhanov.service.api;

import java.util.UUID;

public interface CRUDService<T> {
    T create(T t);

    T getById(UUID uuid);

    T update(T t);

    boolean delete(UUID uuid);
}
