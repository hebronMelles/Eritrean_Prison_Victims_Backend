package Eritrean.Prison.Victims.Controller;

import Eritrean.Prison.Victims.DTOMapper.DtoMapper;
import Eritrean.Prison.Victims.Entity.User;
import Eritrean.Prison.Victims.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final DtoMapper dtoMapper;


    public UserController(UserService userService, DtoMapper dtoMapper) {
        this.userService = userService;
        this.dtoMapper = dtoMapper;

    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        System.out.println("🔍 Fetching user with ID: " + id);
        User user = userService.getUserById(id);
        return user != null ? ResponseEntity.ok(dtoMapper.userToConceleadDto(user)) : ResponseEntity.notFound().build();
    }

    @GetMapping("/tokens")
    public String getTokens(
            @RegisteredOAuth2AuthorizedClient("cognito") OAuth2AuthorizedClient authorizedClient,
            @AuthenticationPrincipal OidcUser oidcUser) {

        String accessToken = authorizedClient.getAccessToken().getTokenValue();
        String idToken = oidcUser.getIdToken().getTokenValue();

        return "Access Token: " + accessToken + "\n\nID Token: " + idToken;
    }


    @GetMapping("/me")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal Object principal) {
        User user = userService.getCurrentUser(principal);
        //   return user != null ? ResponseEntity.ok(dtoMapper.userToConceleadDto(user)) : ResponseEntity.status(401).body("User not authenticated");
        return user != null ? ResponseEntity.ok(dtoMapper.userToConceleadDto(user)) : ResponseEntity.status(401).body("User not authenticated");
    }


    @PutMapping("/upload")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    public String updateUserPhoto(@RequestParam("file") MultipartFile file, @AuthenticationPrincipal Object principal) throws IOException {
        return userService.updateUserPhoto(file, principal);
    }

    @GetMapping("/access-token")
    // @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    public String getAccessToken(
            @RegisteredOAuth2AuthorizedClient("cognito") OAuth2AuthorizedClient authorizedClient,
            @AuthenticationPrincipal OAuth2User oauth2User) {

        String token = authorizedClient.getAccessToken().getTokenValue();
        String email = oauth2User.getAttribute("email");
        String username = oauth2User.getAttribute("username");
        return "Access Token: " + token;
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    @GetMapping("/display-identity")
    public ResponseEntity<String> displayUserIdentity(@AuthenticationPrincipal Object principal) {
        userService.displayUserIdentity(principal);
        return ResponseEntity.ok("User made available successfully");
    }

    @GetMapping("/hide-identity")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<String> hideUserIdentity(@AuthenticationPrincipal Object principal) {
        userService.hideUserIdentity(principal);
        return ResponseEntity.ok("User made available successfully");
    }

    @GetMapping("/report-by-users")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    public String getAllUsersByCount() {
        return userService.getAllUsersByCount();
    }


    @PostMapping("/add-current-to-user")
    public String addCurrentUserToGroup(@AuthenticationPrincipal Jwt jwt) {
        // Try both cognito:username and username claims
        String cognitoUsername = jwt.getClaimAsString("cognito:username");
        if (cognitoUsername == null) {
            cognitoUsername = jwt.getClaimAsString("username");
        }

        if (cognitoUsername == null) {
            throw new IllegalArgumentException("❌ No Cognito username found in token claims");
        }

        userService.addUserToUserGroup(cognitoUsername);

        return "✅ Current user " + cognitoUsername + " added to User group!";
    }


}
