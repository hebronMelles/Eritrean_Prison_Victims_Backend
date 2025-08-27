package Eritrean.Prison.Victims.Security;

import Eritrean.Prison.Victims.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {
    private final UserService userService;

    @Autowired
    public SecurityConfiguration(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        CognitoLogoutHandler cognitoLogoutHandler = new CognitoLogoutHandler();

        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/", "/background.jpg", "/styles/**", "/css/**", "/js/**", "/images/**", "/github-webhook/").permitAll()
                        //  .requestMatchers("/api/users/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/users/me").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/admin/**").authenticated()
                        .requestMatchers("/api/messages/**").authenticated()
                        .requestMatchers("/api/user-forms/**").authenticated()

                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo.oidcUserService(oidcUserService()))
                        .successHandler(customSuccessHandler(userService))
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                )
                .logout(logout -> logout.logoutSuccessHandler(cognitoLogoutHandler));

        return http.build();
    }


    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("cognito:groups");
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            List<String> groups = jwt.getClaimAsStringList("cognito:groups");

            if (groups == null || groups.isEmpty()) {
                // ⚡ fallback to Cognito API
                String username = jwt.getClaimAsString("cognito:username");
                if (username == null) {
                    username = jwt.getClaimAsString("username");
                }

                if (username != null) {
                    groups = userService.getGroupsForUser(username);
                    System.out.println("✅ [JWT Fallback] Resolved groups from Cognito API: " + groups);
                }
            } else {
                System.out.println("✅ [JWT] Using groups directly from token: " + groups);
            }

            if (groups == null) groups = List.of();

            return groups.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                    .collect(Collectors.toSet());
        });

        return converter;
    }


    @Bean
    public OidcUserService oidcUserService() {
        OidcUserService delegate = new OidcUserService();

        return new OidcUserService() {
            @Override
            public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
                // Let Spring load the basic OIDC user first
                OidcUser oidcUser = delegate.loadUser(userRequest);

                // Try to read groups directly from token
                List<String> groups = oidcUser.getClaimAsStringList("cognito:groups");

                if (groups == null || groups.isEmpty()) {
                    // ⚡ fallback: fetch groups from Cognito if token has none
                    String username = oidcUser.getClaim("cognito:username");
                    if (username == null) {
                        username = oidcUser.getClaim("username"); // backup
                    }

                    if (username != null) {
                        groups = userService.getGroupsForUser(username);
                        System.out.println("✅ [OIDC Fallback] Resolved groups from Cognito API: " + groups);
                    } else {
                        groups = Collections.emptyList();
                        System.out.println("⚠️ Could not resolve username from token, no groups assigned");
                    }
                } else {
                    System.out.println("✅ [OIDC] Using groups directly from token: " + groups);
                }

                // Map groups → ROLE_*
                Set<GrantedAuthority> mappedAuthorities = groups.stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                        .collect(Collectors.toSet());

                System.out.println("🚨 Mapped Cognito groups: " + groups);
                System.out.println("🚨 Mapped authorities: " + mappedAuthorities);

                // Return user with mapped authorities
                return new DefaultOidcUser(mappedAuthorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
            }
        };
    }


    @Bean
    public AuthenticationSuccessHandler customSuccessHandler(UserService userService) {
        return (request, response, authentication) -> {
            if (authentication.getPrincipal() instanceof OAuth2User oauthUser) {
                String cognitoUsername = (String) oauthUser.getAttributes().get("cognito:username");
                if (cognitoUsername == null) {
                    cognitoUsername = (String) oauthUser.getAttributes().get("username");
                }

                if (cognitoUsername != null) {
                    // 🔎 Check groups first
                    List<String> groups = userService.getGroupsForUser(cognitoUsername);

                    if (!groups.contains("USER")) {
                        userService.addUserToUserGroup(cognitoUsername);
                        System.out.println("✅ Added " + cognitoUsername + " to User group");
                    } else {
                        System.out.println("ℹ️ " + cognitoUsername + " already in User group, skipping");
                    }
                }
            }

            // Redirect after login
            response.sendRedirect("/api/users/access-token");
        };
    }


    @Bean
    public JwtDecoder jwtDecoder() {
        return JwtDecoders.fromIssuerLocation("https://cognito-idp.us-east-1.amazonaws.com/us-east-1_2drt5vFAs");
    }
}









