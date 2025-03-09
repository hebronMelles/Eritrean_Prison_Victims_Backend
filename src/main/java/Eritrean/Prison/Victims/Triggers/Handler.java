package Eritrean.Prison.Victims.Triggers;

import Eritrean.Prison.Victims.Entity.User;
import Eritrean.Prison.Victims.Service.UserService;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.CognitoUserPoolPostConfirmationEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Map;

public class Handler implements RequestHandler<CognitoUserPoolPostConfirmationEvent, CognitoUserPoolPostConfirmationEvent> {

    private final UserService userService;

    public Handler() {
        ApplicationContext context = new AnnotationConfigApplicationContext("Eritrean.Prison.Victims");
        this.userService = context.getBean(UserService.class);
    }

    @Override
    public CognitoUserPoolPostConfirmationEvent handleRequest(CognitoUserPoolPostConfirmationEvent event, Context context) {
        context.getLogger().log(" Processing Cognito Sign-up Event...");

        try {
            if (event == null || event.getRequest() == null || event.getRequest().getUserAttributes() == null) {
                throw new IllegalArgumentException(" Invalid Cognito Event: Missing user attributes.");
            }

            // Extract Cognito user attributes
            Map<String, String> userAttributes = event.getRequest().getUserAttributes();
            String email = userAttributes.getOrDefault("email", "unknown");
            String firstName = userAttributes.getOrDefault("given_name", "unknown");
            String lastName = userAttributes.getOrDefault("family_name", "unknown");
            String phone = userAttributes.getOrDefault("phone_number", "unknown");

            context.getLogger().log(" User Signed Up: " + firstName + " " + lastName + " (" + email + "), Phone: " + phone);

            // Create a new user entity and save it to MySQL
            User user = new User();
            user.setEmail(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPhone(phone);

            userService.createUser(user);
            context.getLogger().log(" User saved successfully in MySQL.");

        } catch (Exception e) {
            context.getLogger().log(" Error in Lambda: " + e.getMessage());
            throw new RuntimeException("Lambda execution failed: " + e.getMessage());
        }

        return event;
    }
}
