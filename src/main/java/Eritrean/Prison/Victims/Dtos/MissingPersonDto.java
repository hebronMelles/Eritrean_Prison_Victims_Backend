package Eritrean.Prison.Victims.Dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MissingPersonDto {
    private String firstName;
    private String lastName;
    private String age;
    private String description;
    private String locationLastSeen;
    private String contactNumber;
    private String photoUrl;
}
