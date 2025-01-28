package Raiffeisen.Onboarding.Controllers;

import Raiffeisen.Onboarding.Entities.User;
import Raiffeisen.Onboarding.Entities.user_checklists;
import Raiffeisen.Onboarding.Repository.UserChecklistRepository;
import Raiffeisen.Onboarding.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user-checklists")
public class user_ChecklistenController {

    @Autowired
    private UserChecklistRepository userChecklistRepository;

    @Autowired
    private UserRepository userRepository;

    // Get all user checklists
    @GetMapping
    public List<user_checklists> getAllUserChecklists() {
        return (List<user_checklists>) userChecklistRepository.findAll();
    }

    // Get a specific user checklist by ID
    @GetMapping("/{id}")
    public ResponseEntity<user_checklists> getUserChecklistById(@PathVariable String id) {
        return userChecklistRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Create a new user checklist
    @PostMapping
    public ResponseEntity<user_checklists> createUserChecklist(@RequestBody user_checklists newChecklist) {
        user_checklists savedChecklist = userChecklistRepository.save(newChecklist);
        return ResponseEntity.ok(savedChecklist);
    }

    // Update a user checklist
    @PutMapping("/{id}")
    public ResponseEntity<user_checklists> updateUserChecklist(
            @PathVariable String id, @RequestBody user_checklists updatedChecklist) {
        return userChecklistRepository.findById(id).map(existingChecklist -> {
            updatedChecklist.setId(existingChecklist.getId());
            user_checklists savedChecklist = userChecklistRepository.save(updatedChecklist);
            return ResponseEntity.ok(savedChecklist);
        }).orElse(ResponseEntity.notFound().build());
    }

    // Delete a user checklist
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserChecklist(@PathVariable String id) {
        if (userChecklistRepository.existsById(id)) {
            userChecklistRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


    // Get permissions for a specific user checklist

    @GetMapping("/{checklistId}/permissions")
    public ResponseEntity<?> getPermissionsForChecklist(@PathVariable String checklistId) {
        Optional<user_checklists> checklist = userChecklistRepository.findById(checklistId);
        if (checklist.isPresent()) {

            return ResponseEntity.ok(checklist.get().getUserPermissions());
        }
        return ResponseEntity.notFound().build();
    }

    // Remove a permission for a user
    @DeleteMapping("/{checklistId}/permissions")
    public ResponseEntity<Void> removePermission(
            @PathVariable String checklistId, @RequestParam String userId) {
        Optional<user_checklists> checklist = userChecklistRepository.findById(checklistId);
        Optional<User> user = userRepository.findById(userId);

        if (checklist.isPresent() && user.isPresent()) {
            checklist.get().getUserPermissions().remove(user.get());
            userChecklistRepository.save(checklist.get());
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}
