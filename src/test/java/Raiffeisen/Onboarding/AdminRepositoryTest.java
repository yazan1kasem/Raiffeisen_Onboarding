package Raiffeisen.Onboarding;

import Raiffeisen.Onboarding.Entities.User;
import Raiffeisen.Onboarding.Repository.AdminRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // Bestehende MySQL-Datenbank verwenden
public class AdminRepositoryTest {

    @Autowired
    private AdminRepository adminRepository;

    @Test
    void testSaveAndFindById() {
        // Given: Ein neuer Benutzer
        User user = new User();
        user.setUsername("adminUser"); // Benutzername als ID
        user.setPassword("adminPassword");
        user.setRole(User.Role.ADMIN);
        user.setEnabled(true);

        // When: Speichere den Benutzer
        User savedUser = adminRepository.save(user);

        // Then: Überprüfen, ob der Benutzer gespeichert wurde
        assertThat(savedUser.getUsername()).isEqualTo("adminUser");
        assertThat(savedUser.getRole()).isEqualTo(User.Role.ADMIN);

        // Und: Überprüfen, ob der Benutzer per ID gefunden werden kann
        Optional<User> foundUser = adminRepository.findById("adminUser");
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("adminUser");
    }

    @Test
    void testFindAll() {
        // Clean up the repository before the test
        adminRepository.deleteAll();

        // Given: Zwei Benutzer werden gespeichert
        User user1 = new User();
        user1.setUsername("admin1");
        user1.setPassword("password1");
        user1.setRole(User.Role.ADMIN);

        User user2 = new User();
        user2.setUsername("admin2");
        user2.setPassword("password2");
        user2.setRole(User.Role.ADMIN);

        adminRepository.save(user1);
        adminRepository.save(user2);

        // When: Alle Benutzer abrufen
        Iterable<User> users = adminRepository.findAll();

        // Then: Überprüfen, ob beide Benutzer gefunden wurden
        assertThat(users).hasSize(2);
    }

    @Test
    void testUpdateUser() {
        // Given: Ein Benutzer wird gespeichert
        User user = new User();
        user.setUsername("adminUser");
        user.setPassword("oldPassword");
        user.setRole(User.Role.ADMIN);
        adminRepository.save(user);

        // When: Aktualisiere den Benutzer
        user.setPassword("newPassword");
        User updatedUser = adminRepository.save(user);

        // Then: Überprüfen, ob die Änderungen gespeichert wurden
        assertThat(updatedUser.getPassword()).isEqualTo("newPassword");
    }

    @Test
    void testDeleteUser() {
        // Given: Ein Benutzer wird gespeichert
        User user = new User();
        user.setUsername("adminUser");
        user.setPassword("adminPassword");
        user.setRole(User.Role.ADMIN);
        User savedUser = adminRepository.save(user);

        // When: Lösche den Benutzer
        adminRepository.deleteById("adminUser");

        // Then: Überprüfen, ob der Benutzer gelöscht wurde
        boolean exists = adminRepository.existsById("adminUser");
        assertThat(exists).isFalse();
    }
}