package Eritrean.Prison.Victims.Controller;

import Eritrean.Prison.Victims.Entity.ChatMessage;
import Eritrean.Prison.Victims.Service.ChatMessageService;
import Eritrean.Prison.Victims.Service.MessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageProducer messageProducer;
    private final ChatMessageService chatMessageService;

    @Autowired
    public MessageController(MessageProducer messageProducer, ChatMessageService chatMessageService) {
        this.messageProducer = messageProducer;

        this.chatMessageService = chatMessageService;
    }

    @PostMapping("/send")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<String> sendMessage(@RequestBody ChatMessage message, @AuthenticationPrincipal Object principal) {
        messageProducer.sendMessage(principal, message);
        return ResponseEntity.ok("Message sent to Kafka");
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<ChatMessage>> getAllMessages(@AuthenticationPrincipal Object principal) {
        return ResponseEntity.ok(chatMessageService.getAllMessages(principal));
    }

    @GetMapping("/single/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<ChatMessage> getMessageById(@PathVariable Long id, @AuthenticationPrincipal Object principal) {
        return ResponseEntity.ok(chatMessageService.getMessageById(id, principal));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<String> deleteChatMessage(@AuthenticationPrincipal Object principal, @PathVariable Long id) {
        chatMessageService.deleteChatMessage(principal, id);
        return ResponseEntity.ok("Successfully deleted message");
    }
}

