package Eritrean.Prison.Victims.Entity;

import Eritrean.Prison.Victims.ROLE;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String sub;
    private String photoUrl;
    @OneToMany
    private List<UserForm> userForms = new ArrayList<>();
    private boolean display = false;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ChatMessage> chatMessageList = new ArrayList<>();

}
