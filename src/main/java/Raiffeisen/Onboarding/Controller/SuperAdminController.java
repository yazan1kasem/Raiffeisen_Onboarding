package Raiffeisen.Onboarding.Controller;

import Raiffeisen.Onboarding.Entities.Item;
import Raiffeisen.Onboarding.Entities.User;
import Raiffeisen.Onboarding.Entities.User_Checklists;
import Raiffeisen.Onboarding.Repository.ItemRepository;
import Raiffeisen.Onboarding.Repository.UserChecklistRepository;
import Raiffeisen.Onboarding.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/*
*   This class is responsible for handling requests from the superadmin.
*  The superadmin can promote a user to admin. true
* The superadmin can demote a user to user. true
* The superadmin can set the active status of a user. true
* The superadmin can delete a user. true
* The superadmin can delete all userchecklists of a user.
* The superadmin can delete one userchecklist of a user.
* The superadmin can enable a user to access the checklist.
* The superadmin can revoke a user's access to the checklist.
* The superadmin can block a user.
* The superadmin can unblock a user.
* */



@RestController
@RequestMapping(path = "/superadmin")
@CrossOrigin("*")
public class SuperAdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserChecklistRepository userChecklistRepository;



    @PutMapping("/userpromote/{id}")
    public ResponseEntity<User> promoteUserToAdmin(@PathVariable String id) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setRole(User.Role.ADMIN);
                    User updatedUser = userRepository.save(user);
                    return ResponseEntity.ok(updatedUser);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/userdemote/{id}")
    public ResponseEntity<User> demoteUserToUser(@PathVariable String id) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setRole(User.Role.USER);
                    User updatedUser = userRepository.save(user);
                    return ResponseEntity.ok(updatedUser);
                })
                .orElse(ResponseEntity.notFound().build());
    }



    @DeleteMapping("/userdelete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        if (userRepository.existsById(id)) {
            userChecklistRepository.findByUser(userRepository.findById(id).get()).forEach(userChecklist -> {
                userChecklistRepository.delete(userChecklist);
            });
            userRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/userchecklistdelete/{id}")
    public ResponseEntity<Void> deleteUserChecklist(@PathVariable String id) {
        if (userChecklistRepository.existsById(id)) {
            userChecklistRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/userchecklistdeleteall/{id}")
    public ResponseEntity<Void> deleteallUserChecklistfromuser(@PathVariable String id) {
        if (userRepository.existsById(id)) {
            userChecklistRepository.deleteAll(userChecklistRepository.findByUser(userRepository.findById(id).get()));
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/blockuserchecklist/{id}")
    public ResponseEntity<User_Checklists> blockUserChecklist(@PathVariable String id) {
        return userChecklistRepository.findById(id)
                .map(userChecklists -> {
                    userChecklists.setLocked(true);
                    User_Checklists userChecklists1= userChecklistRepository.save( userChecklists);
                    return ResponseEntity.ok(userChecklists1);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/unblockuserchecklist/{id}")
    public ResponseEntity<User_Checklists> unblockUserChecklist(@PathVariable String id) {
        return userChecklistRepository.findById(id)
                .map(userChecklists -> {
                    userChecklists.setLocked(false);
                    User_Checklists userChecklists1= userChecklistRepository.save( userChecklists);
                    return ResponseEntity.ok(userChecklists1);
                })
                .orElse(ResponseEntity.notFound().build());
    }





    @PutMapping("/users/{id}/grant-access/{isActive}")
    public ResponseEntity<Boolean> grantChecklistAccess(@PathVariable String id, @PathVariable boolean isActive) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = optionalUser.get();
        List<User_Checklists> userChecklists = userChecklistRepository.findByUser(user);

        if (userChecklists.isEmpty()) {
            return ResponseEntity.ok(false); // Kein Update notwendig, da keine Checklisten existieren
        }

        userChecklists.forEach(userChecklist -> userChecklist.setLocked(isActive));
        userChecklistRepository.saveAll(userChecklists);

        return ResponseEntity.ok(true); // Erfolgreiches Update
    }




    @PutMapping("/users/{id}/blockuser/{isActive}")
    public ResponseEntity<Boolean> userenable(@PathVariable String id, @PathVariable boolean isActive) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = optionalUser.get();

        user.setEnabled(isActive);
        userRepository.save(user);

        return ResponseEntity.ok(true); // Erfolgreiches Update
    }




    @GetMapping("/test")
    public ResponseEntity<String> checkIfSuperAdmin(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body("User is not authenticated");
        }

        boolean isSuperadmin = userDetails.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_SUPER_ADMIN"));

        if (isSuperadmin) {
            return ResponseEntity.ok("User is an superadmin");
        } else {
            return ResponseEntity.status(403).body("User is not an superadmin");
        }
    }
}
