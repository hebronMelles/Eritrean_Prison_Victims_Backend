package Eritrean.Prison.Victims.Controller;

import Eritrean.Prison.Victims.Entity.ChatMessage;
import Eritrean.Prison.Victims.Entity.User;
import Eritrean.Prison.Victims.Service.ChatMessageService;
import Eritrean.Prison.Victims.Service.MessageProducer;
import Eritrean.Prison.Victims.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageProducer messageProducer;
    private final UserService userService;
    private final ChatMessageService chatMessageService;
   @Autowired
    public MessageController(MessageProducer messageProducer, UserService userService, ChatMessageService chatMessageService) {
        this.messageProducer = messageProducer;
        this.userService = userService;
       this.chatMessageService = chatMessageService;
   }

    @PostMapping
    public ResponseEntity<String> sendMessage(@RequestBody ChatMessage message, @AuthenticationPrincipal Object principal) {
        messageProducer.sendMessage(principal,message);
        return ResponseEntity.ok("Message sent to Kafka");
    }
    @GetMapping("/all")
    public ResponseEntity<List<ChatMessage>>getAllMessages(@AuthenticationPrincipal Object principal){
        return ResponseEntity.ok(chatMessageService.getAllMessages(principal));
    }
    @GetMapping("/single/{id}")
    public ResponseEntity<ChatMessage> getMessageById(@PathVariable Long id, @AuthenticationPrincipal Object principal){
       return ResponseEntity.ok(chatMessageService.getMessageById(id,principal));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteChatMessage(@AuthenticationPrincipal Object principal, @PathVariable Long id){
       chatMessageService.deleteChatMessage(principal,id);
       return ResponseEntity.ok("Successfully delted message");
    }
}

