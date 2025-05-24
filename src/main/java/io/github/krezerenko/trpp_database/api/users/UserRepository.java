package io.github.krezerenko.trpp_database.api.users;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID>
{
    Optional<User> findByName(String name);
    boolean existsByName(String name);

    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    Optional<User> findByPhoneNumber(String phoneNumber);
    boolean existsByPhoneNumber(String phoneNumber);
}
