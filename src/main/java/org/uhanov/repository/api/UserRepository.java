package org.uhanov.repository.api;

import org.springframework.stereotype.Repository;
import org.uhanov.model.User;

@Repository
public interface UserRepository
        extends CrudRepository<User> {

}
