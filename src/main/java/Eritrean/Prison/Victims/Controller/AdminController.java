package Eritrean.Prison.Victims.Controller;

import Eritrean.Prison.Victims.Entity.User;
import Eritrean.Prison.Victims.Service.AdminService;
import Eritrean.Prison.Victims.Service.UserFormService;
import Eritrean.Prison.Victims.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }
    @PostMapping("/create-user")
    public ResponseEntity<String> createUser(User user) {
      adminService.createUser(user);
      return ResponseEntity.ok("Successfully created user");
    }
    @DeleteMapping("/delete-user")
    public ResponseEntity<String> deleteUser(Object principal) {
       adminService.deleteUser(principal);
       return ResponseEntity.ok("Successfully deleted user");
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
