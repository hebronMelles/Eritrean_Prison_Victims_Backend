package Eritrean.Prison.Victims.Controller;



import Eritrean.Prison.Victims.DTOMapper.DtoMapper;
import Eritrean.Prison.Victims.Report.LocationCount;
import Eritrean.Prison.Victims.Entity.UserForm;
import Eritrean.Prison.Victims.Service.UserFormService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/user-forms")
public class UserFormController {

   private final UserFormService userFormService;
   private final DtoMapper dtoMapper;

  @Autowired
    public UserFormController(UserFormService userFormService, DtoMapper dtoMapper) {
        this.userFormService = userFormService;
        this.dtoMapper = dtoMapper;
    }


    @PostMapping
    public ResponseEntity<UserForm> createUserForm(@RequestBody UserForm userForm,@AuthenticationPrincipal Object principal) {
        return ResponseEntity.ok(userFormService.createUserForm(userForm,principal));
    }

    @GetMapping
    public ResponseEntity<List<UserForm>> getAllUserForms() {
        return ResponseEntity.ok(userFormService.getAllUserForms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserFormById(@PathVariable Long id) {
        UserForm userForm = userFormService.getUserFormById(id);
        return userForm != null ? ResponseEntity.ok(dtoMapper.userFormToDto(userForm)) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserForm> updateUserForm(@PathVariable Long id, @RequestBody UserForm userFormDetails) {
        UserForm updatedUserForm = userFormService.updateUserForm(id, userFormDetails);
        return updatedUserForm != null ? ResponseEntity.ok(updatedUserForm) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserForm(@PathVariable Long id, @AuthenticationPrincipal Object principal) {
        return userFormService.deleteUserForm(id,principal) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/form")
    public String showUserForm(Model model) {
        model.addAttribute("userForm", new UserForm()); // Pass object to Thymeleaf
        return "UserForm";
    }
    @GetMapping("/search")
    public List<UserForm> search(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return userFormService.search(location, startDate,endDate);
    }
    @GetMapping("/report-by-location")
    public List<LocationCount> getUserCountByLocation() {
        return userFormService.getUserCountByLocation();
    }
    @GetMapping("report-by-prisoners")
    public String getTotalNumberOfPrisoners(){
        return userFormService.getTotalNumberOfPrisoners();
    }
}

