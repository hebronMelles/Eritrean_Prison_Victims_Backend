package Eritrean.Prison.Victims.Controller;

import Eritrean.Prison.Victims.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import Eritrean.Prison.Victims.Entity.User;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ✅ Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        System.out.println("🔍 Fetching user with ID: " + id);
        User user = userService.getUserById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    // ✅ Get logged-in user details
    @GetMapping("/me")
    public ResponseEntity<?> getLoggedInUser() {
        System.out.println("🔍 Fetching logged-in user...");
        User user = userService.getLoggedInUser();
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.status(401).body("User not authenticated");
    }
}
