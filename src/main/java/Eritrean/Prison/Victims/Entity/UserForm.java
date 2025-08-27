package Eritrean.Prison.Victims.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class UserForm {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private String location;
    private String description;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

}
