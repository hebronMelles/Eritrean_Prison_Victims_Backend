package Eritrean.Prison.Victims.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Admin {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;


}
