package Eritrean.Prison.Victims.Controller;
import Eritrean.Prison.Victims.Entity.UserForm;
import ch.qos.logback.core.model.Model;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import Eritrean.Prison.Victims.Service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;
import Eritrean.Prison.Victims.Entity.User;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.Authentication;
import java.io.IOException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import java.util.List;
import java.util.Map;
import java.security.Principal;
@RestController
@RequestMapping("/api/users")

public class UserController {
    static String Token ;
    private final UserService userService;
    private final OAuth2AuthorizedClientService authorizedClientService;
    public UserController(UserService userService, OAuth2AuthorizedClientService authorizedClientService) {
        this.userService = userService;
        this.authorizedClientService = authorizedClientService;
    }

    // ✅ Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        System.out.println("🔍 Fetching user with ID: " + id);
        User user = userService.getUserById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }


//    @GetMapping("/me")
//    public ResponseEntity<?> getLoggedInUser() {
//        System.out.println("🔍 Fetching logged-in user...");
//        User user = userService.getLoggedInUser();
//        return user != null ? ResponseEntity.ok(user) : ResponseEntity.status(401).body("User not authenticated");
//    }
@GetMapping("/me")
public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal Object principal) {
//    System.out.println("🔍 Fetching logged-in user...");
//       User user = userService.getLoggedInUser();
//    if (principal instanceof Jwt jwt) {
//        String sub = jwt.getClaimAsString("sub");
//        User user1 = userService.getCurrentUserEmail(sub); // lookup by cognito sub
//        return ResponseEntity.ok(user1);
//    }
//
//
    User user = userService.getCurrentUser(principal);
    return user != null ? ResponseEntity.ok(user) : ResponseEntity.status(401).body("User not authenticated");
}

    @PutMapping("/upload")
    public String updateUserPhoto(@RequestParam("file") MultipartFile file, @AuthenticationPrincipal Object principal) throws IOException {
        return userService.updateUserPhoto(file, principal);
    }
    @GetMapping("/access-token")
    public String getAccessToken(
            @RegisteredOAuth2AuthorizedClient("cognito") OAuth2AuthorizedClient authorizedClient,
            @AuthenticationPrincipal OAuth2User oauth2User) {

        String token = authorizedClient.getAccessToken().getTokenValue();
        String email = oauth2User.getAttribute("email");
        String username = oauth2User.getAttribute("username");

        return "Access Token: " + token + "\nUser Email: " + email;
    }



}
