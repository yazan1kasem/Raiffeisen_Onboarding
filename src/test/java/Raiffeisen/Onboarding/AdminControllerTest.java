package Raiffeisen.Onboarding;

import Raiffeisen.Onboarding.Controller.AdminController;
import Raiffeisen.Onboarding.Entities.Item;
import Raiffeisen.Onboarding.Entities.User;
import Raiffeisen.Onboarding.Repository.ItemRepository;
import Raiffeisen.Onboarding.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AdminControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateItem() {
        Item item = new Item();
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        ResponseEntity<Item> response = adminController.createItem(item);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(item, response.getBody());
    }

    @Test
    void testUpdateItem() {
        String itemId = "1";
        Item existingItem = new Item();
        Item updatedItem = new Item();
        updatedItem.setSuchbegriff("newSuchbegriff");

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(existingItem));
        when(itemRepository.save(existingItem)).thenReturn(updatedItem);

        ResponseEntity<Item> response = adminController.updateItem(itemId, updatedItem);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(updatedItem, response.getBody());
    }

    @Test
    void testDeleteItem() {
        String itemId = "1";
        when(itemRepository.existsById(itemId)).thenReturn(true);

        ResponseEntity<Void> response = adminController.deleteItem(itemId);

        assertEquals(204, response.getStatusCodeValue());
        verify(itemRepository, times(1)).deleteById(itemId);
    }

    @Test
    void testSetActiveStatus() {
        String userId = "1";
        User user = new User();
        user.setEnabled(false);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        ResponseEntity<User> response = adminController.setActiveStatus(userId, true);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(true, response.getBody().isEnabled());
    }

    @Test
    void testDeleteUser() {
        String userId = "1";
        when(userRepository.existsById(userId)).thenReturn(true);

        ResponseEntity<Void> response = adminController.deleteUser(userId);

        assertEquals(204, response.getStatusCodeValue());
        verify(userRepository, times(1)).deleteById(userId);
    }
}