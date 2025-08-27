package Eritrean.Prison.Victims.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class UserFormDto {
    private LocalDate startDate;
    private LocalDate endDate;
    private String location;
    private String description;
    private String numberOfYears;
}
