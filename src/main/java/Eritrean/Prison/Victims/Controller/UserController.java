package Eritrean.Prison.Victims.Controller;
import Eritrean.Prison.Victims.DTOMapper.DtoMapper;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import Eritrean.Prison.Victims.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.*;
import Eritrean.Prison.Victims.Entity.User;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;



import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;


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
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        System.out.println("🔍 Fetching user with ID: " + id);
        User user = userService.getUserById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @GetMapping("/me")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal Object principal) {
        User user = userService.getCurrentUser(principal);
     //   return user != null ? ResponseEntity.ok(dtoMapper.userToConceleadDto(user)) : ResponseEntity.status(401).body("User not authenticated");
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.status(401).body("User not authenticated");
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


    @PutMapping("/upload")
    public String updateUserPhoto(@RequestParam("file") MultipartFile file, @AuthenticationPrincipal Object principal) throws IOException {
        return userService.updateUserPhoto(file, principal);
    }


    @GetMapping("/display-identity")
    public ResponseEntity<String> displayUserIdentity(@AuthenticationPrincipal Object principal){
        userService.displayUserIdentity(principal);
        return ResponseEntity.ok("User made available successfully");
    }
    @GetMapping("/hide-identity")
    public ResponseEntity<String> hideUserIdentity(@AuthenticationPrincipal Object principal){
        userService.hideUserIdentity(principal);
        return ResponseEntity.ok("User made available successfully");
    }

}
