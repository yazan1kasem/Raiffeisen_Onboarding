package Raiffeisen.Onboarding;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import Raiffeisen.Onboarding.Controller.SuperAdminController;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Optional;

class SuperAdminControllerTest {

    @InjectMocks
    private SuperAdminController superAdminController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    private User mockUser;
    private Item mockItem;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockUser = new User("superadmin", "password", null, null, true, User.Role.SUPER_ADMIN);
        mockItem = new Item("Item1", "Type1", "SearchTerm1");
    }

    @Test
    void testCreateItem() {
        when(itemRepository.save(mockItem)).thenReturn(mockItem);

        ResponseEntity<Item> response = superAdminController.createItem(mockItem);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockItem.getName(), response.getBody().getName());
    }

    @Test
    void testUpdateItem() {
        when(itemRepository.findById(mockItem.getId())).thenReturn(Optional.of(mockItem));
        Item updatedItem = new Item("Updated Item", "Type1", "SearchTerm1");
        when(itemRepository.save(any(Item.class))).thenReturn(updatedItem);

        ResponseEntity<Item> response = superAdminController.updateItem(mockItem.getId(), updatedItem);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Updated Item", response.getBody().getName());
    }

    @Test
    void testDeleteItem() {
        when(itemRepository.existsById(mockItem.getId())).thenReturn(true);

        ResponseEntity<Void> response = superAdminController.deleteItem(mockItem.getId());

        assertEquals(204, response.getStatusCodeValue());
    }


    @Test
    void testPromoteUserToAdmin() {
        when(userRepository.findById("superadmin")).thenReturn(Optional.of(mockUser));
        User promotedUser = new User("superadmin", "password", null, null, true, User.Role.ADMIN);
        when(userRepository.save(any(User.class))).thenReturn(promotedUser);

        ResponseEntity<User> response = superAdminController.promoteUserToAdmin("superadmin");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(User.Role.ADMIN, response.getBody().getRole());
    }

    @Test
    void testDemoteUserToUser() {
        mockUser.setRole(User.Role.ADMIN);
        when(userRepository.findById("superadmin")).thenReturn(Optional.of(mockUser));
        User demotedUser = new User("superadmin", "password", null, null, true, User.Role.USER);
        when(userRepository.save(any(User.class))).thenReturn(demotedUser);

        ResponseEntity<User> response = superAdminController.demoteUserToUser("superadmin");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(User.Role.USER, response.getBody().getRole());
    }

    @Test
    void testSetActiveStatus() {
        when(userRepository.findById("superadmin")).thenReturn(Optional.of(mockUser));
        mockUser.setEnabled(true);
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        ResponseEntity<User> response = superAdminController.setActiveStatus("superadmin", true);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isEnabled());
    }

    @Test
    void testDeleteUser() {
        when(userRepository.existsById("superadmin")).thenReturn(true);

        ResponseEntity<Void> response = superAdminController.deleteUser("superadmin");

        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void testCheckIfSuperAdmin_Authenticated() {
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                "superadmin", "password", true, true, true, true,
                List.of(new SimpleGrantedAuthority("ROLE_SUPER_ADMIN"))
        );

        ResponseEntity<String> response = superAdminController.checkIfSuperAdmin(userDetails);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User is an superadmin", response.getBody());
    }

    @Test
    void testCheckIfSuperAdmin_NotAuthenticated() {
        ResponseEntity<String> response = superAdminController.checkIfSuperAdmin(null);

        assertEquals(401, response.getStatusCodeValue());
        assertEquals("User is not authenticated", response.getBody());
    }

    @Test
    void testCheckIfSuperAdmin_NotSuperAdmin() {
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                "user", "password", true, true, true, true,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        ResponseEntity<String> response = superAdminController.checkIfSuperAdmin(userDetails);

        assertEquals(403, response.getStatusCodeValue());
        assertEquals("User is not an superadmin", response.getBody());
    }
}