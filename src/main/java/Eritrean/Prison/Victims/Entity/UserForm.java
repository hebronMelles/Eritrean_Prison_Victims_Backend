package Eritrean.Prison.Victims.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class UserForm {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private String location;
    private String description;

}
