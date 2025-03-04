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