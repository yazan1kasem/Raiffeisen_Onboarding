package Raiffeisen.Onboarding;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import Raiffeisen.Onboarding.Controller.SuperAdminController;
import Raiffeisen.Onboarding.Entities.User;
import Raiffeisen.Onboarding.Entities.User_Checklists;
import Raiffeisen.Onboarding.Repository.UserChecklistRepository;
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
    private UserChecklistRepository userChecklistRepository;

    private User mockUser;
    private User_Checklists mockUserChecklist;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockUser = new User("superadmin", "password", null, null, true, User.Role.SUPER_ADMIN);
        mockUserChecklist = new User_Checklists();
        mockUserChecklist.setId("checklist1");
        mockUserChecklist.setUser(mockUser);
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
    void testDeleteUser() {
        when(userRepository.existsById("superadmin")).thenReturn(true);
        when(userRepository.findById("superadmin")).thenReturn(Optional.of(mockUser));
        when(userChecklistRepository.findByUser(mockUser)).thenReturn(List.of(mockUserChecklist));

        ResponseEntity<Void> response = superAdminController.deleteUser("superadmin");

        assertEquals(204, response.getStatusCodeValue());
        verify(userRepository, times(1)).deleteById("superadmin");
    }

    @Test
    void testDeleteUserChecklist() {
        when(userChecklistRepository.existsById("checklist1")).thenReturn(true);

        ResponseEntity<Void> response = superAdminController.deleteUserChecklist("checklist1");

        assertEquals(204, response.getStatusCodeValue());
        verify(userChecklistRepository, times(1)).deleteById("checklist1");
    }

    @Test
    void testDeleteAllUserChecklists() {
        when(userRepository.existsById("superadmin")).thenReturn(true);
        when(userRepository.findById("superadmin")).thenReturn(Optional.of(mockUser));
        when(userChecklistRepository.findByUser(mockUser)).thenReturn(List.of(mockUserChecklist));

        ResponseEntity<Void> response = superAdminController.deleteallUserChecklistfromuser("superadmin");

        assertEquals(204, response.getStatusCodeValue());
        verify(userChecklistRepository, times(1)).deleteAll(anyList());
    }

    @Test
    void testBlockUserChecklist() {
        when(userChecklistRepository.findById("checklist1")).thenReturn(Optional.of(mockUserChecklist));
        mockUserChecklist.setLocked(true);
        when(userChecklistRepository.save(any(User_Checklists.class))).thenReturn(mockUserChecklist);

        ResponseEntity<User_Checklists> response = superAdminController.blockUserChecklist("checklist1");

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isLocked());
    }

    @Test
    void testUnblockUserChecklist() {
        when(userChecklistRepository.findById("checklist1")).thenReturn(Optional.of(mockUserChecklist));
        mockUserChecklist.setLocked(false);
        when(userChecklistRepository.save(any(User_Checklists.class))).thenReturn(mockUserChecklist);

        ResponseEntity<User_Checklists> response = superAdminController.unblockUserChecklist("checklist1");

        assertEquals(200, response.getStatusCodeValue());
        assertFalse(response.getBody().isLocked());
    }

    @Test
    void testUserEnable() {
        when(userRepository.findById("superadmin")).thenReturn(Optional.of(mockUser));
        mockUser.setEnabled(true);
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        ResponseEntity<Boolean> response = superAdminController.userenable("superadmin", true);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody());
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