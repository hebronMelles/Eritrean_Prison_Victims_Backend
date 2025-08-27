package Eritrean.Prison.Victims.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private List<UserFormDto> userForms = new ArrayList<>();
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String photoUrl;


}
