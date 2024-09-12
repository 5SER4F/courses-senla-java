package org.uhanov.repository.api;

import org.uhanov.model.User;

import java.util.UUID;

public interface UserRepository {
    User get(UUID id);

    User add(User user);

    void update(User user);

    void remove(UUID uuid);

}
