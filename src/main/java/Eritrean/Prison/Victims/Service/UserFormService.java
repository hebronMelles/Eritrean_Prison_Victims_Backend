package Eritrean.Prison.Victims.Service;

import Eritrean.Prison.Victims.Entity.UserForm;
import Eritrean.Prison.Victims.Repository.UserFormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;
@Service
public class UserFormService {
    private final UserFormRepository userFormRepository;

    @Autowired
    public UserFormService(UserFormRepository userFormRepository) {
        this.userFormRepository = userFormRepository;
    }
    // ✅ Create a new UserForm
    public UserForm createUserForm(UserForm userForm) {
        return userFormRepository.save(userForm);
    }

    // ✅ Get all UserForms
    public List<UserForm> getAllUserForms() {
        return userFormRepository.findAll();
    }

    // ✅ Get a single UserForm by ID
    public UserForm getUserFormById(Long id) {
        return userFormRepository.findById(id).orElse(null);
    }

    // ✅ Update UserForm
    public UserForm updateUserForm(Long id, UserForm userFormDetails) {
        Optional<UserForm> existingUserForm = userFormRepository.findById(id);
        if (existingUserForm.isPresent()) {
            UserForm userForm = existingUserForm.get();
            userForm.setStartDate(userFormDetails.getStartDate());
            userForm.setEndDate(userFormDetails.getEndDate());
            userForm.setLocation(userFormDetails.getLocation());
            userForm.setDescription(userFormDetails.getDescription());
            return userFormRepository.save(userForm);
        }
        return null; // Not found
    }

    // ✅ Delete UserForm
    public boolean deleteUserForm(Long id) {
        if (userFormRepository.existsById(id)) {
            userFormRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
