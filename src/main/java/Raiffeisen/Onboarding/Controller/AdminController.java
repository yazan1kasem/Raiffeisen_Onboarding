package Raiffeisen.Onboarding.Controller;

import Raiffeisen.Onboarding.Entities.Item;
import Raiffeisen.Onboarding.Entities.User;
import Raiffeisen.Onboarding.Repository.ItemRepository;
import Raiffeisen.Onboarding.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
                    existingItem.setType(itemDetails.getType());
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

    @GetMapping("/test")
    public ResponseEntity<String> checkIfAdmin(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body("User is not authenticated");
        }

        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            return ResponseEntity.ok("User is an admin");
        } else {
            return ResponseEntity.status(403).body("User is not an admin");
        }
    }
}
