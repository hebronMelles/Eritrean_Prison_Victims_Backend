package Eritrean.Prison.Victims.Controller;

import Eritrean.Prison.Victims.Service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;
import Eritrean.Prison.Victims.Entity.User;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.Authentication;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/users")

public class UserController {
    static String Token ;
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

    @PutMapping("/upload")
    public String updateUserPhoto(@RequestBody MultipartFile file) throws IOException {
        return userService.updateUserPhoto(file);
    }
    @GetMapping("/token")
    public String getAccessToken(Authentication authentication) {
        if (authentication == null) {
            return "User is not authenticated.";
        }

        if (authentication.getPrincipal() instanceof OidcUser oidcUser) {
            String token = oidcUser.getIdToken().getTokenValue();
            Token = token;
            System.out.println("Access Token: " + token); // ✅ Prints to console
            return token; // ✅ Returns token in response
        }

        return "No OAuth2 token found.";
    }
}
