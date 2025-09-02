package Eritrean.Prison.Victims.Controller;

import Eritrean.Prison.Victims.Entity.User;
import Eritrean.Prison.Victims.Service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/create-user")
    public ResponseEntity<String> createUser(User user) {
        adminService.createUser(user);
        return ResponseEntity.ok("Successfully created user");
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/delete-user")
    public ResponseEntity<String> deleteUser(Object principal) {
        adminService.deleteUser(principal);
        return ResponseEntity.ok("Successfully deleted user");
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    @GetMapping("/access-token")
    public String getAccessToken(
            @RegisteredOAuth2AuthorizedClient("cognito") OAuth2AuthorizedClient authorizedClient,
            @AuthenticationPrincipal OAuth2User oauth2User) {

        String token = authorizedClient.getAccessToken().getTokenValue();
        String email = oauth2User.getAttribute("email");
        String username = oauth2User.getAttribute("username");

        return "Access Token: " + token;
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public List<User> getAllUsers() {
        return adminService.getAllUsers();
    }

    @DeleteMapping("/delete-by-user-id/{userId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public void deleteUserById(@PathVariable String userId) {
        adminService.deleteUserById(userId);
    }

}
