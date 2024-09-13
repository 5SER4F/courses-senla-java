package org.uhanov.repository.api;

import org.uhanov.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<User> get(UUID id);

    User add(User user);

    void update(User user);

    void remove(UUID uuid);

}
