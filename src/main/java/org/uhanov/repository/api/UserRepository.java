package org.uhanov.repository.api;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import org.uhanov.exception.ResourceNotFoundException;
import org.uhanov.model.User;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>
// CrudRepository<User>
{
    Optional<User> findByNickname(String nickname);

    default UserDetails findUserDetailsByNickname(String name) {
        return findByNickname(name)
                .orElseThrow(ResourceNotFoundException::new);
    }
    boolean existsByNickname(String nickname);

}
