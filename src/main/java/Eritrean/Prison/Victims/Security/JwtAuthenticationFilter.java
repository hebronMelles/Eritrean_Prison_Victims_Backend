package Eritrean.Prison.Victims.Security;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.io.IOException;
import java.security.interfaces.RSAPublicKey;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwkProvider jwkProvider;

    public JwtAuthenticationFilter() {
        this.jwkProvider = new JwkProviderBuilder("https://cognito-idp.us-east-1.amazonaws.com/us-east-1_2drt5vFAs").build();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Remove "Bearer " prefix

            try {
                DecodedJWT jwt = JWT.decode(token);
                String kid = jwt.getKeyId(); // Get Key ID (kid) from the JWT header

                // Fetch the correct RSA public key from Cognito
                Jwk jwk = jwkProvider.get(kid);
                RSAPublicKey publicKey = (RSAPublicKey) jwk.getPublicKey();

                // Verify JWT using Cognito's public key
                DecodedJWT verifiedJwt = JWT.require(com.auth0.jwt.algorithms.Algorithm.RSA256(publicKey, null))
                        .build()
                        .verify(token);

                // Extract user details
              String userId = verifiedJwt.getClaim("sub").asString(); // Cognito User ID
                System.out.println("🔑 JWT Token: " + token);
                // Store userId in SecurityContext
                User user = new User(userId, "N/A", Collections.emptyList()); // Non-null password required
                PreAuthenticatedAuthenticationToken auth = new PreAuthenticatedAuthenticationToken(user, token, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
