package Raiffeisen.Onboarding.Repository;

import Raiffeisen.Onboarding.Entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, String> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
}
