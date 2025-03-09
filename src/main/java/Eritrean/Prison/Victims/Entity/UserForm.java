package Eritrean.Prison.Victims.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class UserForm {
    @Id
    private Long id;
    private Date startDate;
    private Date endDate;
    private String Location;
    private String description;


}
