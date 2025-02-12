package Raiffeisen.Onboarding.Controller;

import Raiffeisen.Onboarding.Entities.CheckList;
import Raiffeisen.Onboarding.Entities.Item;
import Raiffeisen.Onboarding.Entities.User;
import Raiffeisen.Onboarding.Entities.User_Checklists;
import Raiffeisen.Onboarding.JWT.services.AuthenticationService;
import Raiffeisen.Onboarding.Repository.CheckListenRepository;
import Raiffeisen.Onboarding.Repository.ItemRepository;
import Raiffeisen.Onboarding.Repository.UserChecklistRepository;
import Raiffeisen.Onboarding.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;



/*
    * This class is responsible for handling requests from the admin.
    * The admin can also only get and UPDATE checklisten.
    * The admin can change user password.
    * The admin can also view all the checklisten.
    * The admin can also view all the items.
    * The admin can also view all the users.
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
    private UserChecklistRepository userChecklistRepository;

    @Autowired
    private CheckListenRepository checkListenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //    UPDATE checklisten.
    @PutMapping("/checklist/{id}")
    public ResponseEntity<CheckList> updateCheckListen(
            @PathVariable String id,
            @RequestBody CheckList checkListenDetails) {
        return checkListenRepository.findById(id).map(existingCheckList -> {
            existingCheckList= checkListenDetails;
            CheckList updatedCheckList = checkListenRepository.save(existingCheckList);
            return ResponseEntity.ok(updatedCheckList);
        }).orElse(ResponseEntity.notFound().build());
    }





//    user password.
    @PutMapping("/user/{id}")
    public ResponseEntity<User> changeUserPassword(
            @PathVariable String id,
            @RequestParam String password) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setPassword(passwordEncoder.encode(password));
                    User updatedUser = userRepository.save(user);
                    return ResponseEntity.ok(updatedUser);
                })
                .orElse(ResponseEntity.notFound().build());
    }
    //The admin can also view all the user_checklisten of a user.

    @GetMapping("/userchecklist")
    public @ResponseBody Iterable<User_Checklists> getAllUser_Checklistss(@RequestBody User user) {
        return userChecklistRepository.findByUser(user);
    }

    @GetMapping("/items")
    public @ResponseBody Iterable<Item> getAllItems() {
        return itemRepository.findAll();
    }

    @GetMapping("/users")
    public @ResponseBody Iterable<User> getAllUsers() {
        return userRepository.findAll();
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
