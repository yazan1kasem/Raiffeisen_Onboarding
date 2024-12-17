package Raiffeisen.Onboarding.JWT.dtos;

import Raiffeisen.Onboarding.Entities.User;
import Raiffeisen.Onboarding.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Gibt alle Benutzer zurück.
     */
    public List<User> allUsers() {
        // Konvertiere Iterable zu List
        return StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    /**
     * Aktualisiert die Rolle eines Benutzers.
     *
     * @param id   Benutzer-ID
     * @param role Neue Rolle des Benutzers
     * @return Aktualisierter Benutzer oder null, falls der Benutzer nicht existiert
     */
    public User updateUserRole(String id, String role) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setRole(User.Role.valueOf(role)); // Angenommen, Role ist ein Enum
                    return userRepository.save(user);
                }).orElse(null);
    }
}
