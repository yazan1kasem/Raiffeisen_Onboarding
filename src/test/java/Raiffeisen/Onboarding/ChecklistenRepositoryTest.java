package Raiffeisen.Onboarding;

import Raiffeisen.Onboarding.Entities.CheckList;
import Raiffeisen.Onboarding.Repository.CheckListenRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // Bestehende MySQL-Datenbank verwenden
public class ChecklistenRepositoryTest {

    @Autowired
    private CheckListenRepository checkListenRepository;

    @Test
    void testSaveAndFindById() {
        // Given: Eine neue Checkliste
        CheckList checkList = new CheckList();
        checkList.setPosition("Position 1");
        checkList.setAbteilungsname("Abteilung A");

        // When: Speichere die Checkliste
        CheckList savedCheckList = checkListenRepository.save(checkList);

        // Then: Überprüfen, ob die Checkliste gespeichert wurde
        assertThat(savedCheckList.getId()).isNotNull();
        assertThat(savedCheckList.getPosition()).isEqualTo("Position 1");
        assertThat(savedCheckList.getAbteilungsname()).isEqualTo("Abteilung A");

        // Und: Überprüfen, ob die Checkliste per ID abgerufen werden kann
        Optional<CheckList> foundCheckList = checkListenRepository.findById(savedCheckList.getId());
        assertThat(foundCheckList).isPresent();
        assertThat(foundCheckList.get().getPosition()).isEqualTo("Position 1");
        assertThat(foundCheckList.get().getAbteilungsname()).isEqualTo("Abteilung A");
    }

    @Test
    void testFindAll() {
        // When: Alle Checklisten abrufen
        Iterable<CheckList> checkLists = checkListenRepository.findAll();

        // Then: Überprüfen, ob Checklisten vorhanden sind
        assertThat(checkLists).isNotEmpty();
    }

    @Test
    void testUpdateCheckList() {
        // Given: Speichere eine Checkliste
        CheckList checkList = new CheckList();
        checkList.setPosition("Position 1");
        checkList.setAbteilungsname("Abteilung B");
        CheckList savedCheckList = checkListenRepository.save(checkList);

        // When: Aktualisiere die Checkliste
        savedCheckList.setPosition("Updated Position");
        CheckList updatedCheckList = checkListenRepository.save(savedCheckList);

        // Then: Überprüfen, ob die Änderungen gespeichert wurden
        assertThat(updatedCheckList.getPosition()).isEqualTo("Updated Position");
    }

    @Test
    void testDeleteCheckList() {
        // Given: Speichere eine Checkliste
        CheckList checkList = new CheckList();
        checkList.setPosition("Position 1");
        checkList.setAbteilungsname("Abteilung C");
        CheckList savedCheckList = checkListenRepository.save(checkList);

        // When: Lösche die Checkliste
        checkListenRepository.deleteById(savedCheckList.getId());

        // Then: Überprüfen, ob die Checkliste gelöscht wurde
        boolean exists = checkListenRepository.existsById(savedCheckList.getId());
        assertThat(exists).isFalse();
    }
}
