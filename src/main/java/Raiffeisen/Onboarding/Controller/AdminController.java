package Raiffeisen.Onboarding.Controller;

import Raiffeisen.Onboarding.Entities.CheckList;
import Raiffeisen.Onboarding.Entities.Item;
import Raiffeisen.Onboarding.Entities.User;
import Raiffeisen.Onboarding.Repository.CheckListenRepository;
import Raiffeisen.Onboarding.Repository.ItemRepository;
import Raiffeisen.Onboarding.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


/*
    * This class is responsible for handling requests from the admin.
    * The admin can update, delete, and create items.
    * The admin can also only UPDATE checklisten.
    * The admin can also check user Role.
    * The admin can change user password.
    * The admin can lock and unlock user.
    * The admin can also check if the user is an admin.
    * The admin can also check if the user is authenticated.
    * The admin can also enable and disable user_checklisten.
    * The admin can also view all the checklisten.
    * The admin can also view all the items.
    * The admin can also view all the users.
    * The admin can also view all the user_checklisten.
    * The admin can also view all the user_checklisten of a user.
 */
@RestController
@RequestMapping(path = "/admin")
@CrossOrigin("*")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;


    @Autowired
    private CheckListenRepository checkListenRepository;

    @PutMapping("/{id}")
    public ResponseEntity<CheckList> updateCheckListen(
            @PathVariable String id,
            @RequestBody CheckList checkListenDetails) {
        return checkListenRepository.findById(id).map(existingCheckList -> {
            existingCheckList.setItems(checkListenDetails.getItems());
            CheckList updatedCheckList = checkListenRepository.save(existingCheckList);
            return ResponseEntity.ok(updatedCheckList);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCheckListen(@PathVariable String id) {
        if (checkListenRepository.existsById(id)) {
            checkListenRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

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
