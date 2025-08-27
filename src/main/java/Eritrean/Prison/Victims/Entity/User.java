package Eritrean.Prison.Victims.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String sub;

    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String photoUrl;
    private String role;
    @OneToMany
    private List<UserForm> userForms = new ArrayList<>();
    private boolean display = false;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ChatMessage> chatMessageList = new ArrayList<>();

}
