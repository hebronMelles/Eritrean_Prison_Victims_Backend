package Eritrean.Prison.Victims.Controller;



import Eritrean.Prison.Victims.Entity.UserForm;
import Eritrean.Prison.Victims.Service.UserFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-forms")
public class UserFormController {

   private final UserFormService userFormService;

  @Autowired
    public UserFormController(UserFormService userFormService) {
        this.userFormService = userFormService;
    }


    @PostMapping
    public ResponseEntity<UserForm> createUserForm(@RequestBody UserForm userForm) {
        return ResponseEntity.ok(userFormService.createUserForm(userForm));
    }

    @GetMapping
    public ResponseEntity<List<UserForm>> getAllUserForms() {
        return ResponseEntity.ok(userFormService.getAllUserForms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserForm> getUserFormById(@PathVariable Long id) {
        UserForm userForm = userFormService.getUserFormById(id);
        return userForm != null ? ResponseEntity.ok(userForm) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserForm> updateUserForm(@PathVariable Long id, @RequestBody UserForm userFormDetails) {
        UserForm updatedUserForm = userFormService.updateUserForm(id, userFormDetails);
        return updatedUserForm != null ? ResponseEntity.ok(updatedUserForm) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserForm(@PathVariable Long id) {
        return userFormService.deleteUserForm(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}

