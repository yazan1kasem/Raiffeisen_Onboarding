package Raiffeisen.Onboarding;

import Raiffeisen.Onboarding.Entities.*;
import Raiffeisen.Onboarding.Repository.CheckListenRepository;
import Raiffeisen.Onboarding.Repository.UserChecklistRepository;
import Raiffeisen.Onboarding.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class UserCheckListRepositoryTest {
 /*
    @Autowired
    private UserChecklistRepository userChecklistRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CheckListenRepository checkListRepository;

    private User_Checklists userChecklist;

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .username("user1")
                .password("password")
                .role(User.Role.USER)
                .build();
        userRepository.save(user);

        CheckList originalChecklist = CheckList.builder()
                .id("checklist1")
                .build();
        checkListRepository.save(originalChecklist);

        Item originalItem = Item.builder()
                .id("item1")
                .name("Original Item")
                .build();

        User_Checklist_Items checklistItem = User_Checklist_Items.builder()
                .id("1")
                .originalItem(originalItem)
                .isChecked(false)
                .build();

        userChecklist = User_Checklists.builder()
                .id("1")
                .ueberschrift("Sample Checklist")
                .originalChecklist(originalChecklist)
                .user(user)
                .items(List.of(checklistItem))
                .status(User_Checklists.ChecklistStatus.IN_PROGRESS)
                .viewers(List.of(user))
                .isEditableByOthers(true)
                .isLocked(false)
                .build();

        userChecklistRepository.save(userChecklist);
    }

    @Test
    void testFindById() {
        Optional<User_Checklists> foundUserChecklist = userChecklistRepository.findById(userChecklist.getId());
        assertTrue(foundUserChecklist.isPresent());
        assertEquals(userChecklist.getId(), foundUserChecklist.get().getId());
    }

    @Test
    void testSaveUserChecklist() {
        User_Checklists savedUserChecklist = userChecklistRepository.save(userChecklist);
        assertNotNull(savedUserChecklist);
        assertEquals(userChecklist.getId(), savedUserChecklist.getId());
    }
    @Test
    void testDeleteUserChecklist() {
        userChecklistRepository.save(userChecklist);
        userChecklistRepository.deleteById(userChecklist.getId());
        Optional<User_Checklists> foundUserChecklist = userChecklistRepository.findById(userChecklist.getId());
        assertFalse(foundUserChecklist.isPresent());
    }


   */
}