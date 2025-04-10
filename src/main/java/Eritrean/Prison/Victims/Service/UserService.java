package Eritrean.Prison.Victims.Service;

import Eritrean.Prison.Victims.Entity.User;
import Eritrean.Prison.Victims.Repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final CreateTemporaryPreSignedUrl createTemporaryPreSignedUrl;

    @Autowired
    public UserService(UserRepository userRepository, CreateTemporaryPreSignedUrl createTemporaryPreSignedUrl) {
        this.userRepository = userRepository;
        this.createTemporaryPreSignedUrl = createTemporaryPreSignedUrl;
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    // ✅ Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ✅ Get user by ID (Handles String ID correctly)
    public User getUserById(String id) {
        System.out.println("🔍 Fetching user with ID: " + id);
        return userRepository.findById(id).orElse(null);
    }

    // ✅ Update user
    public User updateUser(User userDetails) {
        Optional<User> existingUser = userRepository.findById(getLoggedInUserId());
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setFirstName(userDetails.getFirstName());
            user.setLastName(userDetails.getLastName());
            return userRepository.save(user);
        }
        return null;
    }

    // ✅ Delete user
    public boolean deleteUser() {
        if (userRepository.existsById(getLoggedInUserId())) {
            userRepository.deleteById(getLoggedInUserId());
            return true;
        }
        return false;
    }

    // ✅ Get logged-in user ID from OAuth2 session (Handles Different Cases)
    public String getLoggedInUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            System.out.println("❌ No authenticated user found.");
            return null;
        }


        System.out.println("✅ Authentication Details: " + authentication.toString());

        Object principal = authentication.getPrincipal();
        System.out.println("🔍 Principal Class: " + principal.getClass().getName());

        if (principal instanceof DefaultOidcUser oidcUser) {
            String userId = oidcUser.getAttribute("sub"); // ✅ Use Cognito's User ID
            System.out.println("✅ OIDC User ID: " + userId);
            return userId;
        }


        System.out.println("❌ User principal not recognized.");
        return null;
    }


    // ✅ Get logged-in user details
    public User getLoggedInUser() {
        String userId = getLoggedInUserId();
        if (userId != null) {
            User user = getUserById(userId);

            if (user == null) {
                // Get details from Cognito
                Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

                if (principal instanceof DefaultOidcUser oidcUser) {
                    System.out.println("🔍 OAuth User Details: " + oidcUser.getAttributes()); // Debugging

                    String email = oidcUser.getAttribute("email");
                    String firstName = oidcUser.getAttribute("given_name"); // First Name
                    String lastName = oidcUser.getAttribute("family_name"); // Last Name
                    String phone = oidcUser.getAttribute("phone_number"); // Phone Number

                    System.out.println("🟢 First Name: " + firstName);
                    System.out.println("🟢 Last Name: " + lastName);
                    System.out.println("🟢 Email: " + email);
                    System.out.println("🟢 Phone: " + phone);
                }
            }
            return user;
        }
        return null;
    }

    public String updateUserPhoto(MultipartFile file, Object principal) throws IOException {
        User user = getCurrentUser(principal);
        String fileType = file.getContentType().split("/")[1];
        String url = createTemporaryPreSignedUrl.generateUploadUrl(fileType);
        String filePath = url.split("\\?")[0];
        URL uploadUrl = new URL(url);
        HttpURLConnection httpURLConnection = (HttpURLConnection) uploadUrl.openConnection();
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod("PUT");
        httpURLConnection.setRequestProperty("Content-Type", file.getContentType());
        file.getInputStream().transferTo(httpURLConnection.getOutputStream());
        int responseCode = httpURLConnection.getResponseCode();
        if (responseCode == 200) {
            user.setPhotoUrl(filePath);
            userRepository.save(user);
            return "File uploaded successfully!";
        } else
            return "Upload failed with response code: " + responseCode;
    }
    //Optional<User> user = userRepository.findById(getLoggedInUserId());
    //   if (user.isPresent()) {
//            String fileType = file.getContentType().split("/")[1];
//            String url = createTemporaryPreSignedUrl.generateUploadUrl(fileType);
//            String filePath = url.split("\\?")[0];
//           URL uploadUrl = new URL(url);
//            HttpURLConnection httpURLConnection  = (HttpURLConnection) uploadUrl.openConnection();
//            httpURLConnection.setDoOutput(true);
//            httpURLConnection.setRequestMethod("PUT");
//            httpURLConnection.setRequestProperty("Content-Type", file.getContentType());
//            file.getInputStream().transferTo(httpURLConnection.getOutputStream());
//            int responseCode = httpURLConnection.getResponseCode();
//            if (responseCode == 200) {
//                user.get().setPhotoUrl(filePath);
//                userRepository.save(user.get());
//               return  "File uploaded successfully!";
//            } else
//              return "Upload failed with response code: " + responseCode;
//        }
//        return"user not found";



    public User getCurrentUser(Object principal) {
        String sub = null;
        if (principal instanceof OidcUser oidcUser) {
            sub = oidcUser.getAttribute("sub");
        } else if (principal instanceof Jwt jwt) {
            sub = jwt.getClaimAsString("sub");
        }

        if (sub == null) {
            throw new UsernameNotFoundException("User sub not found in principal.");
        }
        return userRepository.findBySub(sub);
    }

}
