package Raiffeisen.Onboarding.Controller;

import Raiffeisen.Onboarding.Entities.Item;
import Raiffeisen.Onboarding.Entities.User;
import Raiffeisen.Onboarding.Repository.ItemRepository;
import Raiffeisen.Onboarding.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "/admin")
@CrossOrigin("*")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;



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
                    existingItem.setGeraet(itemDetails.getGeraet());
                    existingItem.setAdministration(itemDetails.getAdministration());
                    existingItem.setSoftware(itemDetails.getSoftware());
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
}
