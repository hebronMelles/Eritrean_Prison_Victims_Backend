package Eritrean.Prison.Victims.Security;

import Eritrean.Prison.Victims.ROLE;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Class to configure AWS Cognito as an OAuth 2.0 authorizer with Spring Security.
 * In this configuration, we specify our OAuth Client.
 * We also declare that all requests must come from an authenticated user.
 * Finally, we configure our logout handler.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        CognitoLogoutHandler cognitoLogoutHandler = new CognitoLogoutHandler();

        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/", "/background.jpg", "/styles/**", "/css/**", "/js/**", "/images/**","/github-webhook/").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/users/upload/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/users/me").hasRole("USER")
                        .requestMatchers(HttpMethod.POST,"/messages/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/users/secure-endpoint").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/users/access-token").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/user-forms/form").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/user-forms/form").authenticated()
                        .requestMatchers(HttpMethod.GET, "/user-from").authenticated()
                        .requestMatchers("/user-form").permitAll()
                        .anyRequest()
                        .authenticated())

//                .oauth2Login(oauth2 -> oauth2
//                        .successHandler(customSuccessHandler()))
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo.oidcUserService(oidcUserService()))
                        .successHandler(customSuccessHandler())
                )


//                .oauth2ResourceServer(oauth2 -> oauth2
//                        .jwt(Customizer.withDefaults())      // THIS enables Bearer token (JWT) support
//                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                )



                .logout(logout -> logout.logoutSuccessHandler(cognitoLogoutHandler));
        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler customSuccessHandler() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new SimpleUrlAuthenticationSuccessHandler("/api/users/me");
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return JwtDecoders.fromIssuerLocation("https://cognito-idp.us-east-1.amazonaws.com/us-east-1_2drt5vFAs");
    }
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("cognito:groups");
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
    @Bean
    public OidcUserService oidcUserService() {
        OidcUserService delegate = new OidcUserService();

        return new OidcUserService() {
            @Override
            public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
                OidcUser oidcUser = delegate.loadUser(userRequest);

                List<String> groups = oidcUser.getClaimAsStringList("cognito:groups");

                if (groups == null) {
                    groups = Collections.emptyList(); // or handle accordingly
                }

                Set<GrantedAuthority> mappedAuthorities = groups.stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .collect(Collectors.toSet());

                return new DefaultOidcUser(mappedAuthorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
            }

        };
    }







}






