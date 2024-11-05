package org.uhanov.repository.api;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import org.uhanov.exception.ResourceNotFoundException;
import org.uhanov.model.user.User;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);

    default UserDetails findUserDetailsByUsername(String name) {
        return findByUsername(name)
                .orElseThrow(ResourceNotFoundException::new);
    }

    boolean existsByUsername(String username);

    boolean existsByUsernameOrEmail(String username, String email);

}
