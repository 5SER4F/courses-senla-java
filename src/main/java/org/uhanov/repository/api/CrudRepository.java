package org.uhanov.repository.api;

import java.util.Optional;
import java.util.UUID;

public interface CrudRepository<T> {
    Optional<T> findById(UUID id);

    T save(T entity);

    T update(T e);

    void deleteById(UUID id);


}
