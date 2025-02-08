package Raiffeisen.Onboarding;

import Raiffeisen.Onboarding.Entities.User;
import Raiffeisen.Onboarding.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // Bestehende MySQL-Datenbank verwenden
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testSaveAndFindByUsername() {
        // Given: Ein neuer Benutzer mit Benutzername als ID
        User user = new User();
        user.setUsername("testuser"); // Benutzername ist gleichzeitig die ID
        user.setPassword("password");
        user.setRole(User.Role.USER);

        // When: Speichere den Benutzer
        User savedUser = userRepository.save(user);

        // Then: Überprüfen, ob der Benutzer gespeichert wurde
        assertThat(savedUser.getUsername()).isNotNull(); // Prüft die ID
        assertThat(savedUser.getUsername()).isEqualTo("testuser");

        // Und: Überprüfen, ob der Benutzer per Benutzername gefunden werden kann
        Optional<User> foundUser = userRepository.findByUsername("testuser");
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getPassword()).isEqualTo("password");
    }

    @Test
    void testExistsByUsername() {
        // Given: Ein Benutzer wird gespeichert
        User user = new User();
        user.setUsername("testuser"); // Benutzername ist gleichzeitig die ID
        user.setPassword("password");
        user.setRole(User.Role.USER);
        userRepository.save(user);

        // When: Überprüfen, ob der Benutzername existiert
        boolean exists = userRepository.existsByUsername("testuser");

        // Then: Der Benutzername sollte existieren
        assertThat(exists).isTrue();

        // Und: Ein nicht vorhandener Benutzername sollte nicht existieren
        boolean notExists = userRepository.existsByUsername("nonexistentuser");
        assertThat(notExists).isFalse();
    }

    @Test
    void testDeleteUserByUsername() {
        // Given: Ein Benutzer wird gespeichert
        User user = new User();
        user.setUsername("testuser"); // Benutzername ist gleichzeitig die ID
        user.setPassword("password");
        user.setRole(User.Role.USER);
        userRepository.save(user);

        // When: Lösche den Benutzer per Benutzername (ID)
        userRepository.deleteById("testuser");

        // Then: Der Benutzer sollte nicht mehr existieren
        boolean exists = userRepository.existsByUsername("testuser");
        assertThat(exists).isFalse();
    }

    @Test
    void testFindAllUsers() {
        // Given: Zwei Benutzer werden gespeichert
        User user1 = new User();
        user1.setUsername("user1"); // Benutzername ist gleichzeitig die ID
        user1.setPassword("password1");
        user1.setRole(User.Role.USER);

        User user2 = new User();
        user2.setUsername("user2"); // Benutzername ist gleichzeitig die ID
        user2.setPassword("password2");
        user2.setRole(User.Role.ADMIN);

        userRepository.save(user1);
        userRepository.save(user2);

        // When: Alle Benutzer abrufen
        Iterable<User> users = userRepository.findAll();

        // Then: Überprüfen, ob beide Benutzer gefunden wurden
        assertThat(users).hasSize(2);
    }
}
