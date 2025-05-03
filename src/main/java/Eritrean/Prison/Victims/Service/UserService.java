package Eritrean.Prison.Victims.Service;

import Eritrean.Prison.Victims.Entity.User;
import Eritrean.Prison.Victims.Entity.UserForm;
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

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(String id) {
        return userRepository.findById(id).orElse(null);
    }


    public String updateUser(User userDetails, Object principal) {
        User existingUser = getCurrentUser(principal);
        if (existingUser != null) {
            existingUser.setFirstName(userDetails.getFirstName());
            existingUser.setLastName(userDetails.getLastName());
            userRepository.save(existingUser);
            return "Success";
        }
        return "Fail";
    }

    // ✅ Delete user
    public String deleteUser(Object principal) {
        User existingUser = getCurrentUser(principal);
        if (existingUser != null) {
            userRepository.delete(existingUser);
            return "Success";
        }
        return "Fail";
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
    public void deleteUserFormFromUser(UserForm userForm, Object principal) {
        User user = getCurrentUser(principal);
        user.getUserForms().remove(userForm);
    }
}
