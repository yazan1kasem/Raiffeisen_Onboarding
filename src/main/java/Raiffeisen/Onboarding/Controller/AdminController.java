package Raiffeisen.Onboarding.Controller;

import Raiffeisen.Onboarding.Entities.CheckList;
import Raiffeisen.Onboarding.Entities.Item;
import Raiffeisen.Onboarding.Entities.User;
import Raiffeisen.Onboarding.Entities.user_checklists;
import Raiffeisen.Onboarding.Repository.CheckListenRepository;
import Raiffeisen.Onboarding.Repository.ItemRepository;
import Raiffeisen.Onboarding.Repository.UserChecklistRepository;
import Raiffeisen.Onboarding.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/admin")
@CrossOrigin("*")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserChecklistRepository userChecklistRepository;

    @Autowired
    private CheckListenRepository checklistRepository;
    @PostMapping("/items")
    public ResponseEntity<Item> createItem(@RequestBody Item item) {
        Item savedItem = itemRepository.save(item);
        return ResponseEntity.ok(savedItem);
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<Item> updateItem(
            @PathVariable String id,
            @RequestBody Item itemDetails) {
        return itemRepository.findById(id)
                .map(existingItem -> {
                    existingItem.setSuchbegriff(itemDetails.getSuchbegriff());
                    Item updatedItem = itemRepository.save(existingItem);
                    return ResponseEntity.ok(updatedItem);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable String id) {
        if (itemRepository.existsById(id)) {
            itemRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/users/{id}/status")
    public ResponseEntity<User> setActiveStatus(
            @PathVariable String id,
            @RequestParam boolean isActive) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setEnabled(isActive);
                    User updatedUser = userRepository.save(user);
                    return ResponseEntity.ok(updatedUser);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/users/{id}/promote")
    public ResponseEntity<User> promoteUserToAdmin(@PathVariable String id) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setRole(User.Role.ADMIN);
                    User updatedUser = userRepository.save(user);
                    return ResponseEntity.ok(updatedUser);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/users/{id}/demote")
    public ResponseEntity<User> demoteUserToUser(@PathVariable String id) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setRole(User.Role.USER);
                    User updatedUser = userRepository.save(user);
                    return ResponseEntity.ok(updatedUser);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/users/{id}/grant-access")
    public ResponseEntity<User> grantChecklistAccess(@PathVariable String id) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setAccountNonLocked(true);
                    User updatedUser = userRepository.save(user);
                    return ResponseEntity.ok(updatedUser);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/users/{id}/revoke-access")
    public ResponseEntity<User> revokeChecklistAccess(@PathVariable String id) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setAccountNonLocked(false);
                    User updatedUser = userRepository.save(user);
                    return ResponseEntity.ok(updatedUser);
                })
                .orElse(ResponseEntity.notFound().build());
    }



    @PutMapping("/user_checklists/{id}/lock")
    public ResponseEntity<user_checklists> lockUserChecklist(@PathVariable String id) {
        return userChecklistRepository.findById(id)
                .map(userChecklist -> {
                    userChecklist.setLocked(true);
                    user_checklists updatedChecklist = userChecklistRepository.save(userChecklist);
                    return ResponseEntity.ok(updatedChecklist);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/user_checklists/{id}/unlock")
    public ResponseEntity<user_checklists> unlockUserChecklist(@PathVariable String id) {
        return userChecklistRepository.findById(id)
                .map(userChecklist -> {
                    userChecklist.setLocked(false);
                    user_checklists updatedChecklist = userChecklistRepository.save(userChecklist);
                    return ResponseEntity.ok(updatedChecklist);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Add or update permission for a user
    @PostMapping("/{checklistId}/permissions")
    public ResponseEntity<user_checklists> addOrUpdatePermission(
            @PathVariable String checklistId,
            @RequestParam String userId,
            @RequestParam boolean canEdit
    ) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return userChecklistRepository.findById(checklistId)
                .map(userChecklist -> {
                    userChecklist.getUserPermissions().put(user.get(), canEdit ? user_checklists.Permissions.Read_and_WRITE : user_checklists.Permissions.Read_only);
                    user_checklists updatedChecklist = userChecklistRepository.save(userChecklist);
                    return ResponseEntity.ok(updatedChecklist);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/checklists/{id}")
    public ResponseEntity<CheckList> updateChecklist(
            @PathVariable String id,
            @RequestBody CheckList checklistDetails) {
        return checklistRepository.findById(id)
                .map(existingChecklist -> {
                    existingChecklist.setUeberschrift(checklistDetails.getUeberschrift());
                    existingChecklist.setPosition(checklistDetails.getPosition());
                    existingChecklist.setAbteilungsname(checklistDetails.getAbteilungsname());
                    CheckList updatedChecklist = checklistRepository.save(existingChecklist);
                    return ResponseEntity.ok(updatedChecklist);
                })
                .orElse(ResponseEntity.notFound().build());
    }

}
