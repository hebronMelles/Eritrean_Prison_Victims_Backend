package Eritrean.Prison.Victims.Service;

import Eritrean.Prison.Victims.DTOMapper.DtoMapper;
import Eritrean.Prison.Victims.Entity.User;
import Eritrean.Prison.Victims.Entity.UserForm;
import Eritrean.Prison.Victims.Report.LocationCount;
import Eritrean.Prison.Victims.Repository.UserFormRepository;
import Eritrean.Prison.Victims.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserFormService {
    private final UserFormRepository userFormRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final DtoMapper dtoMapper;

    @Autowired
    public UserFormService(UserFormRepository userFormRepository, UserService userService, UserRepository userRepository, DtoMapper dtoMapper) {
        this.userFormRepository = userFormRepository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.dtoMapper = dtoMapper;
    }


    public UserForm createUserForm(UserForm userForm, Object principal) {
        User user = userService.getCurrentUser(principal);
        List<UserForm> userForm1 = user.getUserForms();
        userForm.setUser(user);
        userFormRepository.save(userForm);
        userForm1.add(userForm);
        userRepository.save(user);
        return userForm;
    }


    public List<UserForm> getAllUserForms() {
        return userFormRepository.findAll();
    }


    public UserForm getUserFormById(Long id) {
        return userFormRepository.findById(id).orElse(null);
    }


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
        return null;
    }


    public boolean deleteUserForm(Long id, Object principal) {
        if (userFormRepository.existsById(id)) {
            UserForm userForm = getUserFormById(id);
            User user = userForm.getUser();
            if (user != null) {
                user.getUserForms().remove(userForm);
                userForm.setUser(null);
            }

            userService.deleteUserFormFromUser(getUserFormById(id), principal);
            userFormRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<UserForm> search(String location, LocalDate startDate, LocalDate endDate) {
        Specification<UserForm> spec = Specification.where(UserFormSpecification.hasLocation(location)
                .and(UserFormSpecification.hasStartDate(startDate))
                .and(UserFormSpecification.hasEndDate(endDate))
        );
        return userFormRepository.findAll(spec);
    }

    public List<LocationCount> getUserCountByLocation() {
        return userFormRepository.getUserFormsByLocation();
    }

    public String getTotalNumberOfPrisoners() {
        return "Total number of Prisoners " + userFormRepository.getAllPrisoners();
    }
}
