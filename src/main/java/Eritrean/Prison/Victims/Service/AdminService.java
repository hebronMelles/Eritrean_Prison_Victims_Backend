package Eritrean.Prison.Victims.Service;

import Eritrean.Prison.Victims.Entity.User;
import Eritrean.Prison.Victims.Repository.UserFormRepository;
import Eritrean.Prison.Victims.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    private final UserRepository userRepository;
    private final UserFormRepository userFormRepository;
    private final UserService userService;

    @Autowired
    public AdminService(UserRepository userRepository, UserFormRepository userFormRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userFormRepository = userFormRepository;
        this.userService = userService;
    }

    public void createUser(User user) {
        userRepository.save(user);
    }

    public String deleteUser(Object principal) {
        User existingUser = userService.getCurrentUser(principal);
        if (existingUser != null) {
            userRepository.delete(existingUser);
            return "Success";
        }
        return "Fail";
    }


}
