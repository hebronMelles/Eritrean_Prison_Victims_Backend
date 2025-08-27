package Eritrean.Prison.Victims.Security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Cognito has a custom logout url.
 * See more information <a href="https://docs.aws.amazon.com/cognito/latest/developerguide/logout-endpoint.html">here</a>.
 */
public class CognitoLogoutHandler extends SimpleUrlLogoutSuccessHandler {

    /**
     * The domain of your user pool.
     */
    private final String domain = "https://prison.auth.us-east-1.amazoncognito.com";

    /**
     * An allowed callback URL.
     */
    private final String logoutRedirectUrl = "http://localhost:8181/"; // Redirect to Cognito login


    /**
     * The ID of your User Pool Client.
     */
    private final String userPoolClientId = "1truinrsmh6jhu1e6fqemu6hh3";

    /**
     * Here, we must implement the new logout URL request. We define what URL to send our request to, and set out client_id and logout_uri parameters.
     */
    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String logoutUrl = UriComponentsBuilder
                .fromUriString(domain + "/logout")
                .queryParam("client_id", userPoolClientId)
                .queryParam("logout_uri", logoutRedirectUrl)
                .encode()
                .toUriString();
        return logoutUrl;
    }
}