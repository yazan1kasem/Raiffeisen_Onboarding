package Raiffeisen.Onboarding.Controller;

import Raiffeisen.Onboarding.Entities.User_Checklist_Items;
import Raiffeisen.Onboarding.Entities.User_Checklists;
import Raiffeisen.Onboarding.Repository.UserChecklistItemsRepository;
import Raiffeisen.Onboarding.Repository.UserChecklistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "/userchecklist")
@CrossOrigin("*")
public class UserChecklistController {

    @Autowired
    private UserChecklistRepository userChecklistRepository;
    @Autowired
    private UserChecklistItemsRepository userChecklistItemsRepository;
    @GetMapping("")
    public @ResponseBody Iterable<User_Checklists> getAllUser_Checklistss() {
        return userChecklistRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User_Checklists> getUser_Checklists(@PathVariable String id) {
        Optional<User_Checklists> userChecklist = userChecklistRepository.findById(id);
        return userChecklist.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("")
    public ResponseEntity<User_Checklists> createUser_Checklists(@RequestBody User_Checklists userChecklist) {
        User_Checklists savedUser_Checklists = userChecklistRepository.save(userChecklist);
        for(User_Checklist_Items userChecklistItems : userChecklist.getItems()){
            userChecklistItemsRepository.save(userChecklistItems);
        }
        return ResponseEntity.ok(savedUser_Checklists);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User_Checklists> updateUser_Checklists(
            @PathVariable String id,
            @RequestBody User_Checklists userChecklistDetails) {
        return userChecklistRepository.findById(id).map(existingUser_Checklists -> {
            existingUser_Checklists.setItems(userChecklistDetails.getItems());
            User_Checklists updatedUser_Checklists = userChecklistRepository.save(existingUser_Checklists);
            return ResponseEntity.ok(updatedUser_Checklists);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser_Checklists(@PathVariable String id) {
        if (userChecklistRepository.existsById(id)) {
            userChecklistRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}