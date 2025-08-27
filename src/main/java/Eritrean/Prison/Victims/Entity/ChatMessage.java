package Eritrean.Prison.Victims.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String senderId;
    private String recipientId;
    private String message;
    private LocalDateTime timestamp;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

}

