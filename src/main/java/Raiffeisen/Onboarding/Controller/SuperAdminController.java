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


@RestController
@RequestMapping(path = "/superadmin")
@CrossOrigin("*")
public class SuperAdminController {

    @Autowired
    private UserRepository userRepository;





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
                    user.setEnabled(true);
                    User updatedUser = userRepository.save(user);
                    return ResponseEntity.ok(updatedUser);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/users/{id}/revoke-access")
    public ResponseEntity<User> revokeChecklistAccess(@PathVariable String id) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setEnabled(false);
                    User updatedUser = userRepository.save(user);
                    return ResponseEntity.ok(updatedUser);
                })
                .orElse(ResponseEntity.notFound().build());
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
